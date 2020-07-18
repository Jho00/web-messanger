import base_url from "@/common/constants/URL_BASE";
import config from "@/common/constants/config";

const url: string = `http://${base_url}:${config.identity_port}`;

const IDENTITY_POINTS = {
    IDENTITY: `${url}/user`
};

export default IDENTITY_POINTS;