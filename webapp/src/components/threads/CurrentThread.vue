<template>
    <div class="current-thread-wrapper" ref="scroll-wrapper">
        <div class="thread-not-present" v-show="!isCurrentThreadPresent">
            <h3>Начните общение прямо сейчас!</h3>
            <el-button type="primary" @click="handleClickStartDialog" round>Написать</el-button>
        </div>
        <div v-show="isCurrentThreadPresent" class="messages">
            <el-input
                    ref="search-input"
                    v-on:focus="searchActive = true"
                    v-on:blur="searchActive = false"
                    class="message-search-element"
                    :class="{'message-search-element-active': searchActive}"
                    placeholder="Поиск"
                    v-model="messageSearch">
                <i slot="prefix" class="el-input__icon el-icon-search" @click="handleSearchIconClick"/>
            </el-input>
            <message v-for="(message, index)  in messages" :message="message" :key="index"></message>
            <el-button class="to-threads-btn" @click="goToThreads" type="success" icon="el-icon-caret-left" circle/>
            <print-message-element v-on:message-sent="handleSetThread" class="print-message"/>
        </div>
    </div>
</template>

<script lang="ts">
import {Component, Vue, Watch, Ref} from "vue-property-decorator";
import ThreadsModule from "@/store/modules/threads/ThreadsModule";
import PrintMessageElement from './current-thread/PrintMessageElement.vue'
import Message from './current-thread/Message.vue'
import IMessage from "@/common/types/IMessage";
import {ElInput} from "element-ui/types/input";

@Component({
    components: {
        PrintMessageElement, Message
    }
})
export default class CurrentThread extends Vue {
    @Ref('scroll-wrapper')
    private scrollabe!: HTMLElement

    @Ref('search-input')
    private searchInput!: ElInput


    private messageSearch: string = ''

    private searchActive: boolean = false;

    get currentThread() {
        return ThreadsModule.activeThread
    }

    get isCurrentThreadPresent() {
        return !!ThreadsModule.activeThread
    }

    get messages(): IMessage[] | undefined {
        if (this.isCurrentThreadPresent) {
            // @ts-ignore
            const messages = this.currentThread?.getMessages;
            if (!this.messageSearch) {
                return messages;
            }

            const search = this.messageSearch.toLowerCase();
            return messages.filter((item: IMessage) => item.content.toLowerCase().includes(search));
        }

        return [];
    }

    handleClickStartDialog() {
        ThreadsModule.setSearchMode(true)
    }

    @Watch('currentThread', { immediate: true })
    handleSetThread() {
        setTimeout(() => {
            const height = this.scrollabe.scrollHeight;
            this.scrollabe.scroll({
                top: height,
                behavior: 'auto'
            })
        }, 400);
    }

    goToThreads() {
        ThreadsModule.setThread(-1);
    }

    checkEvent(e: CustomEvent) {
        if (this.currentThread!.id == e.detail.thread_id) {
            this.handleSetThread();
        }
    }

    handleSearchIconClick() {
        this.searchActive = true;
        this.searchInput.focus();
    }

    mounted() {
        this.handleSetThread();
        //@ts-ignore
        window.addEventListener('updated-thread', this.checkEvent)
    }

    beforeDestroy() {
        // @ts-ignore
        window.removeEventListener('updated-thread', this.checkEvent)
    }
}
</script>

<style scoped lang="less">
    .to-threads-btn {
        display: none;
    }
    .current-thread-wrapper {
        overflow-y: auto;
        width: 100%;
        height: 100%;
        background: #fdefd6;
        display: flex;
        .thread-not-present {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
        }
        .messages {
            .message-search-element {
                z-index: 100000;
                position: absolute;
                right: 10px;
                top: 120px;
                width: 120px;
                transition: width .3s;
            }
            .message-search-element-active {
                width: 240px !important;
            }
            height: max-content;
            margin-bottom: 220px;
            padding-top: 10px;

            .print-message {
                position: fixed;
                bottom: 10px;
                margin-left: 10px;
                width: 67%;
            }
        }
    }

    @media screen and (max-width: 550px) {
        .print-message {
            width: 95% !important;
        }

        .to-threads-btn {
            display: block;
            position: fixed;
            bottom: 70px;
            right: 16px;
        }

        .message-search-element {
            width: 40px !important;
        }
    }
</style>