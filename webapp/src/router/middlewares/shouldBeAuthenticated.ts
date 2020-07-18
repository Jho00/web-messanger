import {Route} from "vue-router";
import AppModule from "@/store/modules/app/AppModule";
import ROUTES from "@/common/constants/ROUTES";

function shouldBeAuthenticated (to: Route, from: Route, next: any) {
    if(AppModule.isAuthenticated) {
        next();
    } else {
        next({
            path: ROUTES.LOGIN,
            replace: true,
        })
    }
}

export default shouldBeAuthenticated;