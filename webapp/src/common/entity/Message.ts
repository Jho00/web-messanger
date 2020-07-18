import IMessage from "@/common/types/IMessage";

class Message implements IMessage{
    authorId: number;
    nickname: string;
    content: string;
    datetime: number;
    attachments: string[];

    constructor(data: IMessage) {
        this.nickname =  data.nickname;
        this.content =  data.content;
        this.datetime =  data.datetime;
        this.authorId = data.authorId;
        this.attachments = data.attachments;
    }
}

export default Message;