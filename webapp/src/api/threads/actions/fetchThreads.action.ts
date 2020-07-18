import axios from 'axios'
import LOGIC_POINTS from "@/api/LOGIC_POINTS";
import IThread from "@/common/types/IThread";

const fetchThreads = async (threadId?: number): Promise<IThread[]> => axios.get(LOGIC_POINTS.FETCH_THREADS, {
    params: {
        threadId: threadId
    }
}).then(result => result.data)

export default fetchThreads;