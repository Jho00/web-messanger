import {Route} from "vue-router";
import AppModule from "@/store/modules/app/AppModule";
import ROUTES from "@/common/constants/ROUTES";

function shouldBeGuest (to: Route, from: Route, next: any) {
    if(!AppModule.isAuthenticated) {
        next();
    } else {
        next({
            path: ROUTES.HOME,
            replace: true,
        })
    }
}

export default shouldBeGuest;