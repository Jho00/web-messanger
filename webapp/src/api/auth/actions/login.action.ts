import axios from 'axios';
import IDENTITY_POINTS from "@/api/IDENTITY_POINTS";
import {ILoginResponse} from "@/common/types/types";

const login = async (nickname: string, password: string): Promise<ILoginResponse> =>
    axios.put(IDENTITY_POINTS.IDENTITY, {
        nickname: nickname,
        password: password
    }).then(result => result.data);

export default login;