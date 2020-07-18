import IFriendRequest from "@/common/types/IFriendRequest";
import {SettingsForm} from "@/common/types/types";

export default interface IFriendsModule {
    requests: IFriendRequest[];
    friends: SettingsForm[];
}