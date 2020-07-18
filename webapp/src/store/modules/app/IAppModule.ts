import {SettingsForm} from "@/common/types/types";

export default interface IAppModule {
    isAuthenticated: boolean,
    isLoading: boolean,
    authKey: string,
    nickname: string,
    email: string,
    id: number,
    settings: SettingsForm | null
}