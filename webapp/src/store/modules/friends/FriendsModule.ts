import {Module, VuexModule, getModule, Mutation, Action} from "vuex-module-decorators";
import StoreModules from "@/store/StoreModules";
import store from "@/store";
import IFriendsModule from "@/store/modules/friends/IFriendsModule";
import api from "@/api/api";
import IFriendRequest from "@/common/types/IFriendRequest";
import {Notification} from "element-ui";
import {SettingsForm} from "@/common/types/types";

@Module({
    dynamic: true,
    name: StoreModules.Friends,
    store,
})
class FriendsModule extends VuexModule implements IFriendsModule{
    requests: IFriendRequest[] = [];
    friends: SettingsForm[] = [];

    @Mutation
    setRequests(requests: IFriendRequest[]) {
        this.requests = requests;
    }

    @Mutation
    setFriends(friends: SettingsForm[]) {
        this.friends = friends;
    }

    @Mutation
    removeRequestById(id: number) {
        const idx = this.requests.findIndex((item) => item.id == id);
        this.requests.splice(idx, 1);
    }

    @Action
    async fetch() {
        api.friends.fetchRequests().then(requests => this.context.commit('setRequests', requests)).catch(error => console.log(error))
        api.friends.fetchFriends().then(friends => this.context.commit('setFriends', friends)).catch(error => console.log(error))
    }

    @Action
    async acceptRequest(id: number) {
        try {
            const result = await api.friends.requests.acceptRequest(id);
            if (result) {
                this.context.commit('removeRequestById', id);
                this.fetch();
            }
        } catch (e) {
            console.log(e);
            Notification.error("Не удалось принять заявку");
        }
    }

    @Action
    async declineRequest(id: number) {
        try {
            const result = await api.friends.requests.declineRequest(id);
            if (result) {
                this.context.commit('removeRequestById', id);
                this.fetch();
            }
        } catch (e) {
            console.log(e);
            Notification.error("Не удалось отклонить заявку");
        }
    }

    get friendRequests() {
        return this.requests;
    }

    get allFriends() {
        return this.friends;
    }
}

export default getModule(FriendsModule);