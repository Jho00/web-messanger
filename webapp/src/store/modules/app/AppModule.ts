import {Module, VuexModule, getModule, Mutation, Action} from "vuex-module-decorators";
import IAppModule from "@/store/modules/app/IAppModule";
import StoreModules from "@/store/StoreModules";
import store from "@/store";
import LOCAL_STORAGE_KEYS from "@/common/constants/LOCAL_STORAGE_KEYS";
import api from "@/api/api";
import IUser from "@/common/types/IUser";
import {Notification} from "element-ui";
import Ws from "@/common/Ws";
import {SettingsForm} from "@/common/types/types";
@Module({
    dynamic: true,
    name: StoreModules.App,
    store,
})
class AppModule extends VuexModule implements IAppModule{
    isAuthenticated = false;
    isLoading = false;

    authKey = '';
    email = '';
    nickname = '';

    id = -1;
    settings: SettingsForm | null = null;

    constructor(props: any) {
        super(props);

        this.authKey = localStorage.getItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN) || '';
        this.isAuthenticated = !!this.authKey;
    }

    @Mutation
    public setIsLoading(value: boolean): void {
        this.isLoading = value;
    }

    @Mutation
    // @ts-ignore
    public setUserInfo({authKey, email, nickname}): void {
        this.authKey = authKey;
        this.email = email;
        this.nickname = nickname;
        if (!!authKey) {
            this.isAuthenticated = true;
        }

        localStorage.setItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN, authKey);
    }

    @Mutation
    public updateProfileInfo(info: IUser) {
        this.nickname = info.nickname;
        this.email = info.email;
        this.id = info.id;

        const event = new Event('userUpdated');
        document.dispatchEvent(event);
    }

    @Mutation
    public updateSettings(settings: SettingsForm) {
        this.settings = settings;
        this.isAuthenticated = true;
    }

    @Action
    public async logout() {
        await api.auth.logout(this.authKey);

        localStorage.removeItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN);
        window.location.reload();
    }

    @Action
    public isLogined() {
        if (!this.isAuthenticated) {
            return false;
        }

        api.auth.isLogined(this.authKey).then(result => {
            //@ts-ignore
            if (!result.logined) {
                localStorage.removeItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN);
                this.authKey = '';
                this.isAuthenticated = false;
                window.location.reload();
                return;
            }

            this.fetchUser();
            this.fetchSettings();
        });
    }

    @Action
    public async fetchUser() {
        console.log('fetch user');
        // @ts-ignore
        const context  = this.context;
        try {
            const result = await api.users.fetchUser();
            context.commit('updateProfileInfo', result);

            const ws = Ws.getInstance();
            ws.init(result.id);
        } catch (e) {
            console.error(e);
            Notification.error("Не удалось получить данные пользователя")
        }
    }

    @Action
    public async fetchSettings() {
        const context = this.context;
        try {
            const result = await api.users.fetchSettings();
            context.commit('updateSettings', result);

        } catch (e) {
            console.error(e);
            Notification.error("Не удалось получить данные пользователя")
        }
    }

    @Action
    public async resetPassword(id: number) {
        try {
            await api.users.resetPassword(id);
            Notification.success("Успешно")
        } catch (e) {
            console.error(e);
            Notification.error("Не удалось получить данные пользователя")
        }
    }
}
export default getModule(AppModule);