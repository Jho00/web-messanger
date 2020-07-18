import axios from 'axios'
import LOGIC_POINTS from "@/api/LOGIC_POINTS";
import IUser from "@/common/types/IUser";

const find = async (q: string): Promise<IUser[]> =>
    axios.get(LOGIC_POINTS.FIND, {
        params: {
            q: q
        }
    }).then(result => result.data);
export default find;