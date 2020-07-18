import auth from "@/api/auth/auth.api";
import users from "@/api/users/users.api";
import threads from "@/api/threads/threads.api";
import {ApiStructure} from "@/common/types/types";
import messages from "@/api/messages/messages.api";
import friends from "@/api/friends/friends.api";

const api: ApiStructure = {
    auth,
    users,
    threads,
    messages,
    friends
};

export default api;