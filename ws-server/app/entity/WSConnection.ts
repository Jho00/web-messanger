import {connection} from "websocket";
import {IMessage, WSActions} from "../common/types";
import ConnectionManager from "../managers/ConnectionManager";

class WSConnection {
    private _userId: number;
    private wsConnection: connection;

    constructor(id: number, connection: any) {
        this._userId = id;
        this.wsConnection = connection;

        this.wsConnection.on('close', () => {
            const manager = ConnectionManager.getInstance();
            manager.removeConnection(this._userId);
        })
    }

    public handleNewMessage(message: IMessage): void {
        const data = {
            action: WSActions.NEW_MESSAGE,
            data: message
        }
        this.wsConnection.sendUTF(JSON.stringify(data))
    }

    public handleNewThread(): void {
        const data = {
            action: WSActions.NEW_THREAD,
        }
        this.wsConnection.sendUTF(JSON.stringify(data))
    }

    public handleNewRequest(): void {
        const data = {
            action: WSActions.NEW_REQUEST,
        }
        this.wsConnection.sendUTF(JSON.stringify(data))
    }

    public getId(): number {
        return this._userId;
    }
}

export default WSConnection;