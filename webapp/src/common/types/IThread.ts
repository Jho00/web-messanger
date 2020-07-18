import IMessage from "@/common/types/IMessage";
import IUser from "@/common/types/IUser";

interface IThread  {
    lastMessageTime: number,
    threadName?: string,
    users: IUser[],
    messages: IMessage[],
    id: number,
    activities: any // like a map <number, number> where key - user id and value - last activity timestamp

    getThreadName(toLower?: boolean): string,
}

export default IThread;