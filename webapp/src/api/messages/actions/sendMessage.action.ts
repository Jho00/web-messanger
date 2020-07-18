import axios from 'axios';
import LOGIC_POINTS from "@/api/LOGIC_POINTS";
const sendMessage = async (content: string, threadId: number, filesUrls: string[]):Promise<Object> => axios.post(LOGIC_POINTS.SEND_MESSAGE, {
    content: content,
    threadId: threadId,
    urls: filesUrls
}).then(result => result.data)

export default sendMessage;