import axios from 'axios';
import LOGIC_POINTS from "../../LOGIC_POINTS";
import {SettingsForm} from "@/common/types/types";

const fetchFriends = async (): Promise<SettingsForm[]> => axios.get(LOGIC_POINTS.FRIENDS).then(result => result.data);

export default fetchFriends;