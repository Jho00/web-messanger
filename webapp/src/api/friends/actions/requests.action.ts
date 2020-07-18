import axios from "axios";
import LOGIC_POINTS from "../../LOGIC_POINTS";

const makeRequest = async (id: number, url: string):Promise<boolean> => axios.post(url, {},{
    params: {
        requestId: id
    }
}).then(result => result.data);

const acceptRequest = async (id: number):Promise<boolean> => makeRequest(id, LOGIC_POINTS.ACCEPT_REQUEST);
const declineRequest = async (id: number):Promise<boolean> => makeRequest(id, LOGIC_POINTS.DECLINE_REQUEST);

const requests = {
    acceptRequest,
    declineRequest
};
export default requests;