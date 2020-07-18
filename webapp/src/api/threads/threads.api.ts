import createThread from "@/api/threads/actions/createThread.action";
import fetchThreads from "@/api/threads/actions/fetchThreads.action";
import seenThread from "@/api/threads/actions/seenThread.action";

const threads = {
    createThread,
    fetchThreads,
    seenThread
}

export default threads;