import isLogined from "@/api/auth/actions/isLodigined.action";
import login from "@/api/auth/actions/login.action";
import signup from "@/api/auth/actions/signup.action";
import logout from "@/api/auth/actions/logout.action";

const auth = {
    isLogined,
    login,
    signup,
    logout
};

export default auth;