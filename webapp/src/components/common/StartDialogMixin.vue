<script lang="ts">
    import {Component,  Vue} from 'vue-property-decorator';
    import ThreadsModule from "../../store/modules/threads/ThreadsModule";
    import ROUTES from "@/common/constants/ROUTES";
    import {IStartDialogMixin} from "@/common/types/types";
    import IThread from "@/common/types/IThread";

    @Component
    export default class StartDialogMixin extends Vue implements IStartDialogMixin{
        private loadingMessageAction: boolean = false;

        async handleStartMessaging() {
            if (this.tryOpenExisted()) {
                return;
            }
            this.loadingMessageAction = true;
            // @ts-ignore
            await ThreadsModule.createThread([this.user.id]);
            this.loadingMessageAction = false;

            this.tryOpenExisted();
        }

        tryOpenExisted() {
            const potentialThread =
                ThreadsModule.allThreads.find((item: IThread) =>
                    item.users.length < 2 &&
                        //@ts-ignore
                    item.users[0].nickname === this.user.nickname);
            if (potentialThread) {
                ThreadsModule.setThread(potentialThread.id);
                this.$router.push(ROUTES.THREADS);
                return true;
            }

            return false;
        }
    }
</script>