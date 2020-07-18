import Vue from 'vue'
import VueRouter from 'vue-router'
import Me from '../views/Me.vue'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import Threads from '../views/Threads.vue'
import Friends from '../views/Friends.vue'
import Profile from '../views/Profile.vue'
import Reset from '../views/Reset.vue'
import NotFoundView from '../views/NotFoundView.vue'
import ROUTES from "@/common/constants/ROUTES";
import shouldBeAuthenticated from "@/router/middlewares/shouldBeAuthenticated";
import shouldBeGuest from "@/router/middlewares/shouldBeGuest";
import shouldBeAdmin from "@/router/middlewares/shouldBeAdmin";

Vue.use(VueRouter);

const routes = [
  {
    path: ROUTES.HOME,
    name: 'Home',
    component: Threads,
    beforeEnter: shouldBeAuthenticated
  },
  {
    path: ROUTES.ME,
    name: 'Me',
    component: Me,
    beforeEnter: shouldBeAuthenticated
  },
  {
    path: ROUTES.LOGIN,
    name: 'Login',
    component: Login,
    beforeEnter: shouldBeGuest
  },
  {
    path: ROUTES.SIGNUP,
    name: 'Signup',
    component: Signup,
    beforeEnter: shouldBeGuest
  },
  {
    path: ROUTES.THREADS,
    name: 'Threads',
    component: Threads,
    beforeEnter: shouldBeAuthenticated
  },
  {
    path: ROUTES.FRIENDS,
    name: 'Friends',
    component: Friends,
    beforeEnter: shouldBeAuthenticated
  },
  {
    path: ROUTES.PROFILE_BY_USERNAME,
    props: true,
    name: 'Profile',
    component: Profile,
    beforeEnter: shouldBeAuthenticated
  },
  {
    path: ROUTES.RESET_USER_PASSWORD,
    name: 'Reset',
    component: Reset,
    beforeEnter: shouldBeAdmin
  },
  {
    path: '*',
    name: 'Not found',
    component: NotFoundView,
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router
