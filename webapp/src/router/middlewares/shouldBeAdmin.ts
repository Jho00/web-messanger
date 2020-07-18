import {Route} from "vue-router";
import AppModule from "@/store/modules/app/AppModule";
import ROUTES from "@/common/constants/ROUTES";

function shouldBeAdmin (to: Route, from: Route, next: any) {
    if(AppModule.isAuthenticated && AppModule.settings?.isAdmin) {
        next();
    } else {
        next({
            path: ROUTES.HOME,
            replace: true,
        })
    }
}

export default shouldBeAdmin;