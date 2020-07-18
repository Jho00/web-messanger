import WS_ACTIONS from "@/common/constants/WS_ACTIONS";
import {WsNewMessage, WsPayload} from "@/common/types/types";
import ThreadsModule from "@/store/modules/threads/ThreadsModule";
import base_url from "@/common/constants/URL_BASE";
import FriendsModule from "@/store/modules/friends/FriendsModule";

class Ws {
    private static instance: Ws = new Ws();

    public static getInstance(): Ws {
        return this.instance;
    }

    private socket: WebSocket | null = null;
    private intervalClose: any;
    private intervalError: any;

    private constructor() {}

    public init(userId: number) {
        this.socket = new WebSocket(`ws://${base_url}:1337`);

        this.socket.addEventListener('close', (event: CloseEvent) => {
            this.intervalClose = setInterval(this.init, 2000);
        });

        this.socket.addEventListener('error', (event: Event) => {
            this.intervalError = setInterval(this.init, 3000);
        });

        this.socket.addEventListener('open', (event: Event) => {
            // @ts-ignore
            this.socket.send(JSON.stringify({
                action: WS_ACTIONS.connect,
                data: {
                    userId: userId
                }
            }));

            clearInterval(this.intervalClose);
            clearInterval(this.intervalError);
        });

        this.socket.addEventListener('message', (event: MessageEvent) => {
            const data = event.data;
            if (!data) {
                return;
            }
            const payload: WsPayload = JSON.parse(data);
            this.handlePayload(payload)
        });
    }

    private handlePayload(payload: WsPayload) {
        switch (payload.action) {
            case WS_ACTIONS.newMessage : this.handleNewMessage(payload.data); break;
            case WS_ACTIONS.newThread : this.handleNewThread(); break;
            case WS_ACTIONS.newRequest : this.handleNewRequest(); break;
            default: console.error(`Unknown ws action: ${payload.action}`)
        }
    }

    private handleNewMessage(message: WsNewMessage) {
        message.datetime = new Date().getTime();
        ThreadsModule.handleNewMessage(message);
    }

    private handleNewThread() {
        ThreadsModule.fetchThreads();
    }

    private handleNewRequest() {
        FriendsModule.fetch();
        document.dispatchEvent(new Event('updateFriend'));
    }
}

export default Ws;