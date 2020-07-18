import base_url from "@/common/constants/URL_BASE";
import config from "@/common/constants/config";

const url: string = `http://${base_url}:${config.logic_port}`;

const LOGIC_POINTS = {
    FIND: `${url}/users/find`,
    ME: `${url}/users/me`,
    GET_PROFILE: `${url}/users/profile`,
    RESET_PASSWORD: `${url}/users/resetPassword`,

    CREATE_THREAD: `${url}/threads/create`,
    FETCH_THREADS: `${url}/threads/`,
    MAKE_THREAD_SEEN: `${url}/threads/seen`,

    SEND_MESSAGE: `${url}/messages/send`,

    SETTINGS: `${url}/settings/`,

    FRIENDS: `${url}/friends/`,
    ADD_FRIEND: `${url}/friends/add`,
    DELETE_FRIEND: `${url}/friends/delete`,
    FRIEND_REQUESTS: `${url}/friends/reqs`,
    ACCEPT_REQUEST: `${url}/friends/accept-request`,
    DECLINE_REQUEST: `${url}/friends/decline-request`,
};

export default LOGIC_POINTS;