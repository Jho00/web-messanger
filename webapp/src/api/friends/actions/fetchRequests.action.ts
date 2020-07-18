import axios from 'axios';
import LOGIC_POINTS from "@/api/LOGIC_POINTS";
import IFriendRequest from "@/common/types/IFriendRequest";

const fetchRequests = async ():Promise<IFriendRequest[]> => axios.get(LOGIC_POINTS.FRIEND_REQUESTS).then(result => result.data);

export default fetchRequests;