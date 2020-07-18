<template>
    <div class="thread-list-wrapper">
        <div v-if="hasThreads" class="list">
            <h3 v-show="threadList.length === 0"> Сообщений не найдено </h3>
            <list-transition>
                <div v-show="threadList.length > 0" v-for="thread of threadList" :key="thread.id">
                    <sh-thread @click.native="open(thread.id)" class="sh-thread" :current-thread="thread" ></sh-thread>
                    <el-divider><i class="el-icon-star-on"></i></el-divider>
                </div>
            </list-transition>
        </div>
        <div class="empty-list-state" v-else>
            <h1>У вас пока что нет сообщений</h1>
            <i class="el-icon-folder-remove"></i>
        </div>
        <search-users-modal :visible="searchUsersEnabled"/>
    </div>
</template>

<script lang="ts">
    import {Component, Prop} from "vue-property-decorator";
    import BaseComponent from "@/components/BaseComponent.vue";
    import ThreadsModule from "@/store/modules/threads/ThreadsModule";
    import ShThread from "@/components/threads/ShThread.vue";
    import SearchUsersModal from "@/components/threads/modals/SearchUsersModal.vue"
    import IThread from "@/common/types/IThread";
    import ListTransition from "@/components/common/ListTransition.vue";

    @Component({
        components: {ShThread, SearchUsersModal, ListTransition}
    })
    export default class ThreadList extends BaseComponent {
        @Prop({required: false, default: false})
        private forSearch!:boolean

        @Prop({required: false, default: ''})
        private searchText!:string // for local search in existed dialogs

        get searchUsersEnabled(): boolean {
            return ThreadsModule.isSearchMode;
        }

        get threadList() {
            if (this.searchText.length === 0) {
                return ThreadsModule.allThreads
            }
            const searchText = this.searchText.toLowerCase();
            return ThreadsModule.allThreads.filter((item: IThread) => item.getThreadName(true)?.includes(searchText));
        }

        open(id: number) {
            ThreadsModule.setThread(id);
            ThreadsModule.seenThread(id);
        }

        get hasThreads() {
            return ThreadsModule.threads.length > 0;
        }
    }
</script>

<style scoped lang="less">
.thread-list-wrapper {
    width: 100%;
    height: 100%;
    display: flex;
    overflow-y: auto;
    .list {
        width: 100%;
        display: flex;
        flex-direction: column;
        h3 {
            align-self: center;
            margin-top: 50px;
        }
    }
    .empty-list-state {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        h1 {
            text-align: center;
        }
        i {
            font-size: 100px;
        }
    }
}
</style>