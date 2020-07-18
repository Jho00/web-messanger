import LOGIC_POINTS from "@/api/LOGIC_POINTS";
import axios from 'axios';

const resetPassword = async (userId: number): Promise<boolean> => axios.get(LOGIC_POINTS.RESET_PASSWORD, {
    params: {
        id: userId
    }
})

export default resetPassword;