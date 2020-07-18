<template>
    <el-card class="thread-wrapper" :body-style="{'padding-top': '3px'}">
        <div>
            <h2 class="thread-header">{{threadTitle}}</h2>
            <p>{{lastMessage}}</p>
        </div>
        <div v-show="isUnread" class="unread-indicator"></div>
    </el-card>
</template>

<script lang="ts">
import {Component, Prop} from "vue-property-decorator";
import BaseComponent from "@/components/BaseComponent.vue";
import Thread from "@/common/entity/Thread";

@Component
export default class ShThread extends BaseComponent {
   @Prop({required: true, type: Object})
   private currentThread!: Thread

   get threadTitle() {
       return this.currentThread.getThreadName()
   }

   get lastMessage() {
       const messages = this.currentThread.getMessages;
       if (messages.length === 0) {
           return '...';
       }
       const last = messages[messages.length - 1].content;
       if (last.length === 0) {
           return '...';
       }
       if (last.length <= 30) {
           return last;
       }
       return `${last.slice(0, 29)}...`
   }

   get isUnread() {
       return this.currentThread.isThreadUnread;
   }
}
</script>

<style scoped lang="less">
    @keyframes blink {
        0%   {opacity: 0}
        25%  {opacity: .5}
        50%  {opacity: 1}
        75%  {opacity: .5}
        100% {opacity: 0}
    }

    .thread-wrapper {
        position: relative;
        cursor: pointer;
        width: 95%;
        height: 75px;
        box-shadow: none !important;
        border: none !important;

        h2 {
            margin-bottom: 5px;
        }
        p {
            margin-top: 5px;
            margin-bottom: 0;
        }
        .unread-indicator {
            position: absolute;
            top: 45px;
            right: 0;
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background: #66b1ff;
            animation: blink 3s linear infinite;
        }
    }
</style>