import find from "@/api/users/actions/findUsers.action";
import fetchUser from "@/api/users/actions/fetchUser.action";
import fetchSettings from "@/api/users/actions/fetchSettings.action";
import setSettings from "@/api/users/actions/setSettings.action";
import getProfile from "@/api/users/actions/getProfile.action";
import resetPassword from "@/api/users/actions/resetPassword";

const users = {
    find,
    fetchUser,
    fetchSettings,
    setSettings,
    getProfile,
    resetPassword
};

export default users;