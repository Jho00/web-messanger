import {SettingsForm} from "../../../common/types/types";
import axios from 'axios';
import LOGIC_POINTS from "@/api/LOGIC_POINTS";

const fetchSettings = async (): Promise<SettingsForm> => axios.get(LOGIC_POINTS.SETTINGS).then(data => data.data);

export default fetchSettings;