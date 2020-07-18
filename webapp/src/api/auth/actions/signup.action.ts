import axios from 'axios';
import IDENTITY_POINTS from "@/api/IDENTITY_POINTS";
import {ILoginResponse} from "@/common/types/types";

const signup = async (
    nickname: string,
    email: string,
    password: string,
    repeatedPassword: string,
): Promise<ILoginResponse> => axios.post(IDENTITY_POINTS.IDENTITY, {
    name: nickname,
    email: email,
    password: password,
    repeatedPassword: repeatedPassword
}).then(result => result.data);

export default signup;