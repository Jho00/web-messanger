import axios from 'axios';
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const deleteFriend = async (id: number):Promise<boolean> => axios.delete(LOGIC_POINTS.DELETE_FRIEND, {
    params: {
        id: id
    }
}).then(result => result.data);

export default deleteFriend;