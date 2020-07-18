import Vue from 'vue'
import Vuex from 'vuex'
import IMessangerStore from "@/store/IMessangerStore";

Vue.use(Vuex)

const store = new Vuex.Store<IMessangerStore>({});
export default store;