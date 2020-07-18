import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// @ts-ignore
import locale from 'element-ui/lib/locale/lang/ru-RU'
import api from "@/api/api";
import axios, {AxiosError, AxiosRequestConfig} from 'axios'
import HEADERS from "@/common/constants/HEADERS";

Vue.config.productionTip = false

Vue.use(ElementUI, {locale})

import AppModule from "@/store/modules/app/AppModule";

axios.interceptors.request.use((config: AxiosRequestConfig) => {
  config.headers[HEADERS.AUTH_HEADER] = AppModule.authKey;
  return config;
}, function (error) {
  return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
  if (response.status === 401) {
    AppModule.logout();
    window.location.reload();
  }
  return response;
}, function (error: AxiosError) {
  if (error.message && error.message.includes("401")) {
    window.location.reload();
  }
  return Promise.reject(error);
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

