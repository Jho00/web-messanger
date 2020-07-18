<template>
    <div class="wrapper">
        <div class="view-large" v-if="isLarge">
            <div class="thread-list element">
                <div class="thread-list-header">
                    <el-input
                            clearable
                            placeholder="Поиск по собеседникам"
                            prefix-icon="el-icon-search"
                            v-model="searchText">
                    </el-input>
                    <el-button @click="handlePlusButton" type="primary" icon="el-icon-plus"/>
                </div>
                <thread-list :search-text="searchText"/>
            </div>
            <div class="current-thread element">
                <current-thread></current-thread>
            </div>
        </div>

        <!-- Mobile view-->
        <div class="view-mobile" v-if="!isLarge">
            <div class="thread-list element" v-show="!isThreadActive">
                <div class="thread-list-header">
                    <el-input
                            clearable
                            placeholder="Поиск по собеседникам"
                            prefix-icon="el-icon-search"
                            v-model="searchText">
                    </el-input>
                    <el-button @click="handlePlusButton" type="primary" icon="el-icon-plus"/>
                </div>
                <thread-list :search-text="searchText"/>
            </div>
            <div class="current-thread element" v-show="isThreadActive">
                <current-thread></current-thread>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import {Component, Mixins} from "vue-property-decorator";
import BaseComponent from "@/components/BaseComponent.vue";
import CurrentThread from "@/components/threads/CurrentThread.vue";
import ThreadList from "@/components/threads/ThreadList.vue";
import ThreadsModule from "@/store/modules/threads/ThreadsModule";
import AppModule from "@/store/modules/app/AppModule";
import {IBaseComponent} from "@/common/types/types";
import FriendsModule from "@/store/modules/friends/FriendsModule";

const handleEscEvent = (event: KeyboardEvent) => {
    if (event.key === 'Escape') {
        ThreadsModule.setThread(-1);
    }
}

@Component({
    components: {CurrentThread, ThreadList}
})
export default class Threads extends Mixins<IBaseComponent>(BaseComponent) {
    private searchText: string = ''

    private windowWidth: number = 0;

    handlePlusButton() {
        ThreadsModule.setSearchMode(true)
    }

    mounted() {
        ThreadsModule.fetchThreads();

        document.addEventListener('keydown', handleEscEvent)

        this.windowWidth = window.innerWidth
        window.onresize = () => {
            this.windowWidth = window.innerWidth
        }

        document.querySelector('html')!.style.overflowY = 'hidden';

        FriendsModule.fetch();
    }

    beforeDestroy() {
        document.removeEventListener('keydown', handleEscEvent, true);
        window.onresize = () => {}
        document.querySelector('html')!.style.overflowY = 'scroll';
    }

    get isThreadActive() {
        return !!ThreadsModule.activeThread;
    }

    get isLarge() {
        return this.windowWidth > 550;
    }
}
</script>

<style scoped lang="less">
.wrapper {
    display: flex;
    .view-large {
        display: flex;
        width: 100%;
    }

    .view-mobile {
        display: none;
        width: 100%;
    }

    .thread-list {
        width: 30%;
        .thread-list-header {
            margin-top: 5px;
            width: 100%;
            display: flex;
            /deep/input {
                margin-left: 5px;
            }
            button {
                margin-left: 10px;
                margin-right: 5px;
            }
        }
    }

    .current-thread {
        width: 70%;
    }

    .element {
        height: 100vh;
    }
}

@media screen and (max-width: 550px) {
    .view-large {
        display: none !important;
    }

    .view-mobile {
        display: flex !important;

        .current-thread {
            width: 100%;
        }

        .thread-list {
            width: 100%;
        }
    }
}
</style>