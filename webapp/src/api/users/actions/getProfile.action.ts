import {SettingsForm} from "@/common/types/types";
import axios, {AxiosResponse} from 'axios'
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const getProfile = async (nickname: string): Promise<AxiosResponse<SettingsForm>> => axios.get(LOGIC_POINTS.GET_PROFILE, {
    params: {
        nickname: nickname
    }
})

export default getProfile;