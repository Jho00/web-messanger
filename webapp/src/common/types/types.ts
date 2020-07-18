import IUser from "@/common/types/IUser";
import IThread from "@/common/types/IThread";
import {AxiosResponse} from "axios";
import IFriendRequest from "@/common/types/IFriendRequest";

export interface IBaseComponent {
    api: ApiStructure

    showErrorNotify(data?: NotifyData): void;
    showWarningNotify(data?: NotifyData): void;
    showInfoNotify(data?: NotifyData): void;
    showSuccessNotify(data?: NotifyData): void;
}

export type NotifyData = {
    title: string, message: string
}

export type ILoginResponse = {
    authToken: string
}

export interface ApiStructure {
    auth: {
        isLogined(authKey: string): Promise<Object>;
        login(nickname: string, password: string): Promise<ILoginResponse>;
        signup(nickname: string,
               email: string,
               password: string,
               repeatedPassword: string): Promise<ILoginResponse>;
        logout(authKey: string): Promise<Object>;
    },
    users: {
        find(q: string): Promise<IUser[]>;
        fetchUser(): Promise<IUser>;
        fetchSettings(): Promise<SettingsForm>;
        setSettings(settings: SettingsForm): Promise<AxiosResponse>;
        getProfile(nickname: string): Promise<AxiosResponse<SettingsForm>>;
        resetPassword(userId: number): Promise<boolean>;
    },
    threads: {
        createThread(users: number[], name?: string): Promise<Object>;
        fetchThreads(threadId?: number): Promise<IThread[]>;
        seenThread(threadId: number): Promise<boolean>;
    },
    messages: {
        sendMessage(content: string, threadId: number, filesUrls: string[]):Promise<Object>;
        sendFile(file: File, id: number): Promise<string>;
    },
    friends: {
        add(id: number): Promise<boolean>;
        deleteFriend(id: number): Promise<boolean>;
        fetchRequests(): Promise<IFriendRequest[]>;
        fetchFriends(): Promise<SettingsForm[]>;
        requests: {
            acceptRequest (id: number):Promise<boolean>
            declineRequest (id: number):Promise<boolean>
        }
    }
}

export type WsPayload = {
    action: string,
    data: WsNewMessage
}

export type WsNewMessage = {
    authorId: number;
    nickname: string;
    content: string;
    datetime: number;
    threadId: number;
    attachments: string[];
}

export type SettingsForm = {
    nickname: string,
    email: string,
    firstName: string,
    lastName: string,
    address: string,
    bio: string,
    phone: string,
    id: number,
    friended: boolean,
    sentRequest: boolean,
    isAdmin: boolean
}

export interface IStartDialogMixin {
    handleStartMessaging(): void;
    tryOpenExisted(): void;
}

export interface FileType {
    name: string,
    percentage: number,
    raw: File,
    size: number,
    status: string,
    uid: number
}

export interface MessageModel {
    content: string,
    files: FileType[]
}