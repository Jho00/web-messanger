import IThread from "@/common/types/IThread";
import IMessage from "@/common/types/IMessage";
import IUser from "@/common/types/IUser";
import {WsNewMessage} from "@/common/types/types";
import Message from "@/common/entity/Message";
import AppModule from "@/store/modules/app/AppModule";

class Thread implements IThread {
    lastMessageTime: number;
    messages: IMessage[];
    threadName?: string;
    users: IUser[];
    id: number;
    activities: any = {};

    private sorted: IMessage[] | null = null;
    private isUnread: boolean | null = null;
    private _id: number = -1;

    constructor(data: Omit<IThread, 'getThreadName'>,) {
        this.lastMessageTime = data.lastMessageTime
        this.messages = data.messages
        this.threadName = data.threadName;
        this.users = data.users;
        this.id = data.id;
        this.activities = data.activities;

        document.addEventListener('userUpdated', () => {
            setTimeout(() => {
                this.isUnread = null;
                this._id = AppModule.id;
                }, 1000);
        })
    }

    getThreadName(toLower = false): string {
        if (this.threadName) {
            return toLower ? this.threadName.toLowerCase() : this.threadName;
        }

        if (this.users.length == 1) {
            return toLower ? this.users[0].nickname.toLowerCase() : this.users[0].nickname;
        }

        let result = `${this.users[0].nickname}, ${this.users[1].nickname} `;
        if (this.users.length == 2) {
            return toLower ? result.toLowerCase() : result;
        }

        result = result + `и еще ${this.users.length - 2}`;
        return  toLower ? result.toLowerCase() : result;
    }

    get getMessages(): IMessage[] {
        if (this.sorted === null) {
            this.sorted = this.messages.sort((item1: IMessage, item2: IMessage) => {
                if (item1.datetime < item2.datetime) {
                    return -1;
                }

                if (item1.datetime > item2.datetime) {
                    return 1;
                }

                return 0;
            });
        }
        return this.sorted;
    }

    public addMessage(message: WsNewMessage): void {
        const newMessage = new Message(message);

        this.messages.push(newMessage);
        this.updateActivity(newMessage.authorId, message.datetime);
        this.sorted = null;

        const event = new CustomEvent(`updated-thread`, {
            detail: {
                thread_id: this.id
            }
        });
        window.dispatchEvent(event);
    }

    public get isThreadUnread(): boolean {
        if (this.isUnread === null) {
            if (!this.activities.hasOwnProperty(this.getId())) {
                console.log(`No id: ${this.getId()} in history`);
                this.isUnread = true;
            } else {
                this.isUnread = this.activities[this.getId()] < this.lastMessageTime;
            }
        }
        
        return this.isUnread;
    }

    public updateActivity(authorId: number = -1, time: number | null = null, updateLastMessage = true) {
        if (authorId < 0) {
            authorId = this.getId();
        }
        if (time == null) {
            time = new Date().getTime();
        }

        if (updateLastMessage) {
            this.lastMessageTime = time;
        }
        this.activities[authorId] = time;
        this.isUnread = null;
    }

    private getId(): number {
        if (this._id < 0) {
            this._id = AppModule.id;
            if (this._id < 0) {
                setTimeout(this.getId, 4000);
            }
        }

        return this._id;
    }
}

export default Thread;