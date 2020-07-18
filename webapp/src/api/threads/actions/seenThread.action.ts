import axois from 'axios'
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const seenThread = async (threadId: number): Promise<boolean> => axois.post(LOGIC_POINTS.MAKE_THREAD_SEEN, {}, {
    params: {
        threadId: threadId
    }
})

export default seenThread;