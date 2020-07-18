import axios from 'axios';
import IDENTITY_POINTS from "@/api/IDENTITY_POINTS";

const logout = async (authKey: string): Promise<Object> => axios.delete(IDENTITY_POINTS.IDENTITY, {
    params: {
        authKey: authKey
    }
}).then(result => result.data);

export default logout;