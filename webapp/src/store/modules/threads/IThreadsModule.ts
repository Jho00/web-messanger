import IThread from "@/common/types/IThread";
import Thread from "@/common/entity/Thread";

export default interface IThreadsModule {
    threads: IThread[],
    currentThread: IThread | undefined | Thread
}