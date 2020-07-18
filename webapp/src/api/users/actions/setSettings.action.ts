import {SettingsForm} from "../../../common/types/types";
import axios, {AxiosResponse} from 'axios';
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const setSettings = async (settings: SettingsForm): Promise<AxiosResponse> => axios.put(LOGIC_POINTS.SETTINGS, settings);

export default setSettings;