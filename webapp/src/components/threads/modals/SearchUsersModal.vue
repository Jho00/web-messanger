<template>
    <el-dialog
            title="Начни общаться сейчас!"
            :visible.sync="visible"
            :before-close="handleCloseModal">
        <div>
            <el-input
                    v-loading.lock="isLoading"
                    clearable
                    placeholder="Найти собеседника"
                    prefix-icon="el-icon-search"
                    v-model="searchText">
            </el-input>
            <div v-show="users.length > 0">
                <el-checkbox-group class="searched" v-model="checkList">
                    <el-checkbox v-for="user of users" :key="user.id" :label="user.nickname"/>
                </el-checkbox-group>
            </div>
            <div v-show="users.length === 0 && searchText.length > 0">
                <p>Пользователей не найдено</p>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="danger" @click="handleCloseModal">Отмена</el-button>
            <el-button type="primary" @click="handleCreateThread" :disabled="!canStartDialog">Начать диалог</el-button>
        </span>
    </el-dialog>
</template>

<script lang="ts">
import BaseComponent from "@/components/BaseComponent.vue";
import {Component, Mixins, Prop, Watch} from "vue-property-decorator";
import ThreadsModule from "@/store/modules/threads/ThreadsModule";
import {IBaseComponent} from "@/common/types/types";
import IUser from "@/common/types/IUser";
import User from "@/common/entity/User";
import AppModule from "@/store/modules/app/AppModule";
const d = require('lodash').debounce;

@Component
export default class SearchUsersModal extends Mixins<IBaseComponent>(BaseComponent){
    @Prop({required: true})
    private visible!: boolean

    private searchText: string = ''

    private debounced: Function = d(this.searchUsers, 250);

    private users: IUser[] = []

    private checkedUsers: IUser[] = []

    private checkList: string[] = []

    private handleCloseModal() {
        ThreadsModule.setSearchMode(false);
    }

    @Watch('searchText', {immediate: true})
    private handleSearchUsers() {
        this.debounced();
    }

    @Watch('checkList', {immediate: true})
    private handleCheckUser() {
        this.checkedUsers = this.users.filter((item) => this.checkList.includes(item.nickname))
    }

    private async searchUsers() {
        this.resetState()
        if (this.searchText.length === 0) {
            return
        }

        try {
            AppModule.setIsLoading(true);
            const result = await this.api.users.find(this.searchText);
            this.users = result.map(item => new User(item)).filter((item: User) => item.id !== AppModule.id);
        } catch (e) {
            console.log(e);
            this.showErrorNotify();
        } finally {
            AppModule.setIsLoading(false);
        }
    }

    resetState() {
        this.checkList = [];
        this.users = [];
        this.checkedUsers = [];
    }

    private handleCreateThread() {
        if (!this.canStartDialog) {
            return;
        }
        const ids: number[] = this.checkedUsers.map(item => item.id);
        ThreadsModule.createThread(ids)
        this.resetState();
        this.handleCloseModal();
    }

    get canStartDialog() {
        return this.checkedUsers.length > 0;
    }
}
</script>

<style scoped lang="less">
/deep/.el-dialog {
    width: 30%;
}

.searched {
    display: flex;
    flex-direction: column;
    .el-checkbox {
        margin-top: 5px;
        margin-left: 3px;
        /deep/.el-checkbox__label {
            font-size: 1.5em;
        }
    }
}

@media screen and (max-width: 1275px) {
    /deep/.el-dialog {
        width: 90%;
    }
}
</style>