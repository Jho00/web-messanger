import axois from 'axios'
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const createThread = async (users: number[], name?: string): Promise<Object> => axois.post(LOGIC_POINTS.CREATE_THREAD, {
    users: users,
    name: name
})

export default createThread;