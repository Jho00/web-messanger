import WSConnection from "../entity/WSConnection";
import {IMessage} from "../common/types";

class ConnectionManager {
    private static _instance: ConnectionManager = new ConnectionManager();

    public static getInstance(): ConnectionManager {
        return this._instance;
    }

    private constructor() {}

    private _connections: Map<number, WSConnection> = new Map();

    public addConnection(connection: WSConnection): void {
        this._connections.set(connection.getId(), connection);

        console.log(`connection registered for user: ${connection.getId()}`);
    }

    public removeConnection(userId: number): void {
        this._connections.delete(userId);

        console.log(`connection removed for user: ${userId}`);
    }

    public getConnection(userId: number): WSConnection | undefined {
        return this._connections.get(userId);
    }

    public handleNewMessage(userIds: number[], message: IMessage): void {
        userIds.forEach((item: number) => {
           const connection: WSConnection | undefined = this.getConnection(item);
            if (connection === undefined) {
                return;
            }

            connection.handleNewMessage(message);
        })
    }

    public handleNewThread(userIds: number[]): void {
        userIds.forEach((item: number) => {
            const connection: WSConnection | undefined = this.getConnection(item);
            if (connection === undefined) {
                return;
            }

            connection.handleNewThread();
        })
    }

    public handleNewRequest(userIds: number[]): void {
        userIds.forEach((item: number) => {
            const connection: WSConnection | undefined = this.getConnection(item);
            if (connection === undefined) {
                return;
            }

            connection.handleNewRequest();
        })
    }
}

export default ConnectionManager;