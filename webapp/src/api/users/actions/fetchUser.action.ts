import IUser from "@/common/types/IUser";
import axios from 'axios';
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const fetchUser = async (): Promise<IUser> => axios.get(LOGIC_POINTS.ME).then(result => result.data);

export default fetchUser;