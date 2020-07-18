<template>
    <div class="message-wrapper" :class="{'my': isMyMessage, 'not-my': !isMyMessage}">
        <p class="message-content">{{message.content}}</p>
        <div class="attachments">
            <i v-for="(item, index) in message.attachments" :key="index" @click="download(index)" class="el-icon-document"></i>
        </div>
        <div class="info">
            <p @click="goToAuthorProfile">{{authorNick}}</p>
            <p>{{messageDate}}</p>
        </div>
    </div>
</template>

<script lang="ts">
import {Component, Prop, Vue} from "vue-property-decorator";
import IMessage from "../../../common/types/IMessage";
import AppModule from "@/store/modules/app/AppModule";
import ROUTES from "@/common/constants/ROUTES";
import base_url from "@/common/constants/URL_BASE";
import config from "@/common/constants/config";

@Component
export default class Message extends Vue {
    @Prop({type: Object, required: true})
    private message!: IMessage

    goToAuthorProfile() {
        if (this.message.authorId === AppModule.id) {
            this.$router.push(ROUTES.ME);
        }
        this.$router.push(`/profile/${this.message.nickname}`);
    }

    download(idx: number) {
        window.open(`${this.mediaServerUrl}${this.message.attachments[idx]}`, '_blank');
    }

    get isMyMessage() {
        return this.message.authorId === AppModule.id;
    }

    get messageDate() {
        const date = new Date(this.message.datetime);

        return `${date.getDate()}.${date.getMonth()}.${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`
    }

    get authorNick() {
        return this.message.nickname;
    }

    get mediaServerUrl() {
        return `http://${base_url}:${config.media_port}`
    }

}
</script>

<style scoped lang="less">
@import (css) url('https://fonts.googleapis.com/css?family=Open+Sans:300,400');
.message-wrapper {
    position: relative;
    margin-left: 20px;
    margin-bottom: 10px;
    padding: 4px;
    text-align: left;
    font: 400 .9em 'Open Sans', sans-serif;
    border-radius: 10px;

    min-width: 200px;
    width: max-content;
    max-width: calc(50vw );

    min-height: 20px;
    height: min-content;

    .message-content {
        margin-bottom: 3px;
    }

    .info {
        p {
            margin: 2px;
            text-align: right;
            font-size: 10px;
            &:nth-child(1) {
                cursor: pointer;
                color: blue;
            }
        }
    }

    .attachments {
        .el-icon-document {
            font-size: 1.5em;
            cursor: pointer;
        }
    }

    &:after {
        content: '';
        position: absolute;
        width: 0;
        height: 0;
        border-left: 15px solid transparent;
        border-right: 15px solid transparent;
        top: 0;
        left: -15px;
    }

    &:before {
        content: '';
        position: absolute;
        width: 0;
        height: 0;
        border-left: 16px solid transparent;
        border-right: 16px solid transparent;
        top: -1px;
        left: -17px;
    }

}

.my {
    background: #A8DDFD;
    border: 1px solid #97C6E3;

    &:before {
        border-top: 15px solid #97C6E3;
    }

    &:after {
        border-top: 15px solid #A8DDFD;
    }
}

.not-my {
    background: #f8e896;
    border: 1px solid #dfd087;

    &:before {
        border-top: 15px solid #dfd087;
    }

    &:after {
        border-top: 15px solid #f8e896;
    }
}

</style>