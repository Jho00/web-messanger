import add from "@/api/friends/actions/add.action";
import deleteFriend from "@/api/friends/actions/delete.action";
import fetchRequests from "@/api/friends/actions/fetchRequests.action";
import requests from "@/api/friends/actions/requests.action";
import fetchFriends from "@/api/friends/actions/fetchFriends.action";

const friends = {
    add,
    deleteFriend,
    fetchRequests,
    fetchFriends,
    requests
};

export default friends;