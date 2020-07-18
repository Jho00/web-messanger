import axios from 'axios';
import MEDIA_POINTS from "@/api/MEDIA_POINTS";

const sendFile = async (file: File, id: number): Promise<string> => {
    const formData = new FormData();
    formData.append('attachment', file);
    return axios.post(`${MEDIA_POINTS.UPLOAD}/${id}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        },
    }).then(result => result.data)
}


export default sendFile;