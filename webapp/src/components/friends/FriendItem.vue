<template>
    <div class="friend-item-wrapper">
        <router-link :to="`profile/${user.nickname}`">
            <h4>{{user.nickname}}</h4>
        </router-link>
        <div class="friend-actions">
            <el-popover
                    placement="top-start"
                    title="Написать"
                    width="200"
                    trigger="hover">
                <el-button @click="handleStartMessaging" slot="reference" type="primary" icon="el-icon-s-promotion" round />
            </el-popover>
            <el-popover
                    class="more"
                    placement="top-start"
                    title="Подробнее"
                    width="200"
                    trigger="hover">
                <el-button @click="more" slot="reference" type="info" icon="el-icon-s-order" round/>
            </el-popover>

        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Mixins} from "vue-property-decorator";
    import {IStartDialogMixin, SettingsForm} from "@/common/types/types";
    import StartDialogMixin from "@/components/common/StartDialogMixin.vue";

    @Component
    export default class FriendItem extends Mixins<IStartDialogMixin>(StartDialogMixin){
        @Prop({type: Object, required: true})
        private user!: SettingsForm;

        public more() {
            this.$router.push(`profile/${this.user.nickname}`)
        }
    }
</script>

<style scoped lang="less">
.friend-item-wrapper {
    box-sizing: border-box;
    padding: 15px;
    width: 100%;
    height: 100px;
    opacity: .85;
    display: flex;
    align-items: center;
    position: relative;
    &:hover {
        opacity: 1;
    }
    a {
        text-decoration: none;
        font-size: 1.4em;
        color: black;
        transition: .3s all;
        margin-right: 10px;
        &:hover {
            color: blue;
        }
    }
    .friend-actions {
        position: absolute;
        right: 10px;
        .more {
            margin-left: 5px;
        }
    }
}
</style>