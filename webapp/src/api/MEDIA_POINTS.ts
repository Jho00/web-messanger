import base_url from "@/common/constants/URL_BASE";
import config from "@/common/constants/config";

const url: string = `http://${base_url}:${config.media_port}`;

const MEDIA_POINTS = {
    UPLOAD: `${url}/upload`
};

export default MEDIA_POINTS;