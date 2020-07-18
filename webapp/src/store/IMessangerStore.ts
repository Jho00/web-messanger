import IAppModule from "@/store/modules/app/IAppModule";
import IThreadsModule from "@/store/modules/threads/IThreadsModule";
import IFriendsModule from "@/store/modules/friends/IFriendsModule";

interface IMessangerStore {
    app: IAppModule,
    threads: IThreadsModule,
    friends: IFriendsModule
}

export default IMessangerStore;