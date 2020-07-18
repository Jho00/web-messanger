import {Action, getModule, Module, Mutation, VuexModule} from "vuex-module-decorators";
import StoreModules from "@/store/StoreModules";
import store from "@/store";
import IThreadsModule from "@/store/modules/threads/IThreadsModule";
import IThread from "@/common/types/IThread";
import api from "@/api/api";
import {Notification} from 'element-ui';
import AppModule from "@/store/modules/app/AppModule";
import Thread from "@/common/entity/Thread";
import {FileType, MessageModel, WsNewMessage} from "@/common/types/types";
import IMessage from "@/common/types/IMessage";

@Module({
    dynamic: true,
    name: StoreModules.Threads,
    store,
})
class ThreadsModule extends VuexModule implements IThreadsModule{
    threads: IThread[] = [];
    currentThread: IThread | Thread | undefined = undefined;

    private searchUsersEnabled: boolean = false;

    constructor(props: any) {
        super(props);
        setTimeout(() => {
            if (AppModule.isAuthenticated) {
                this.fetchThreads()
            }
        }, 3000);
    }

    @Mutation
    public setThread(id: number) {
        if (id < 0) {
            this.currentThread = undefined;
            return;
        }
        this.currentThread = this.threads.find((item: IThread) => item.id === id);
    }

    @Mutation
    public setSearchMode(value: boolean = true) {
        this.searchUsersEnabled = value;
    }

    @Mutation
    public setThreads(threads: IThread[]) {
        this.threads = threads.map(item => new Thread(item))
    }

    @Mutation
    public handleNewMessage(message: WsNewMessage) {
        // @ts-ignore
        const thread: Thread | undefined = this.threads.find((item) => item.id === message.threadId)
        if (thread === undefined) {
            console.log(`Can not find thread with id: ${message.threadId}`);
            return;
        }

        thread.addMessage(message);
        if (this.currentThread && this.currentThread.id === thread.id) {
            thread.updateActivity();
        }
    }

    @Mutation
    public updateCurrentActivity() {
        // @ts-ignore
        this.currentThread.updateActivity(-1, null,false);
    }

    @Action
    public async fetchThreads() {
        try {
            const result = await api.threads.fetchThreads();
            this.setThreads(result)
        } catch (e) {
            console.error(e);
            Notification.error({message: 'Попробуйте обновить страницу', title: 'Не удалось получить список сообщений'})
        }
    }

    @Action
    public async createThread(ids: number[], name?: string) {
        if (AppModule.isLoading) {
            return
        }
        try {
            AppModule.setIsLoading(true)
            await api.threads.createThread(ids);
            await this.fetchThreads();
        } catch (e) {
            console.error(e);
            Notification.error("Не удалось начать диалог")
        } finally {
            AppModule.setIsLoading(false)
        }
    }

    @Action
    public async sendFiles(files: FileType[]) {
        return new Promise((resolve, reject) => {
            const urls: string[] = [];
            for (const item of files) {
                api.messages.sendFile(item.raw, AppModule.id).then(url => {
                    urls.push(url);
                    if (urls.length === files.length) {
                        resolve(urls);
                    }
                }).catch(error => {
                    console.log(error);
                    reject();
                })
            }
        });
    }

    @Action
    public async sendMessage(message: MessageModel) {
        // @ts-ignore
        const context: IThreadsModule = this.context.state;
        if (context.currentThread === undefined) {
            console.error('Current thread is not present')
            return
        }
        const threadId: number = context.currentThread?.id;
        try {
            let urls: string[] = [];
            if (message.files.length > 0) {
                // @ts-ignore
                urls = await this.sendFiles(message.files);
            }
            await api.messages.sendMessage(message.content, threadId, urls);
        } catch (e) {
            console.error(e);
            Notification.error("Не удалось отправить сообщение")
        }
    }

    @Action
    public async seenThread(threadId: number) {
        try {
            await api.threads.seenThread(threadId);
            this.context.commit('updateCurrentActivity');
        } catch (e) {
            console.error(e);
            console.log("Не удалось прочитать сообщение")
        }
    }

    get activeThread() {
        return this.currentThread;
    }

    get isSearchMode() {
        return this.searchUsersEnabled;
    }

    get allThreads() {
        return this.threads.sort((item1: IThread, item2: IThread) => {
            if (item1.lastMessageTime > item2.lastMessageTime) {
                return -1;
            }

            if (item1.lastMessageTime < item2.lastMessageTime) {
                return 1;
            }

            return 0;
        });;
    }
}

export default getModule(ThreadsModule);