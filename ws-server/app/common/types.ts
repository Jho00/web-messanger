export interface IUser {
    nickname: string;
    email: string;
    id: number
}

export interface IMessage {
    authorId: number;
    nickname: string;
    content: string;
    datetime: number;
    threadId: number;
    attachments: string[];
}

export enum WSActions {
    NEW_MESSAGE = 'newMessage',
    NEW_THREAD = 'newThread',
    NEW_REQUEST = 'newRequest'
}

export type InitialPayloadType = {
    action: string,
    data: {
        userId: number
    }
}