import axios, {AxiosPromise} from 'axios';
import IDENTITY_POINTS from "@/api/IDENTITY_POINTS";

const isLogined = async (authKey: string): Promise<Object> => axios.get(IDENTITY_POINTS.IDENTITY, {
    params: {
        authKey: authKey
    }
}).then(result => result.data);

export default isLogined;