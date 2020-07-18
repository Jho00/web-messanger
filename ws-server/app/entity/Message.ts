import {IMessage} from "../common/types";

class Message implements IMessage{
    authorId: number;
    content: string;
    datetime: number;
    nickname: string;
    threadId: number;
    attachments: string[];

    constructor(data: IMessage) {
        this.authorId = data.authorId;
        this.content = data.content;
        this.datetime = data.datetime;
        this.nickname = data.nickname;
        this.threadId = data.threadId;
        this.attachments = data.attachments;
    }
}

export default Message;