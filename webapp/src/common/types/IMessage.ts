interface IMessage {
    datetime: number;
    content: string;
    nickname: string;
    authorId: number;
    attachments: string[];
}

export default IMessage;