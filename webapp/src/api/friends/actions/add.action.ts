import axios from 'axios';
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const add = async (id: number):Promise<boolean> => axios.post(LOGIC_POINTS.ADD_FRIEND, {},{
    params: {
        id: id
    }
}).then(result => result.data);

export default add;