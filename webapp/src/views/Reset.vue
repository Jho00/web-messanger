<template>
    <div class="reset-wrapper">
        <el-input
                class="search-user"
                v-loading.lock="loading"
                clearable
                autofocus
                placeholder="Найти пользователя"
                prefix-icon="el-icon-search"
                v-model="searchText">
        </el-input>
        <div class="found-users">
            <reset-user-item v-for="user in users" :key="user.id" :user="user"/>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Mixins, Watch} from "vue-property-decorator";
    import IUser from "@/common/types/IUser";
    import AppModule from "@/store/modules/app/AppModule";
    import User from "@/common/entity/User";
    import {IBaseComponent} from "@/common/types/types";
    import BaseComponent from "@/components/BaseComponent.vue";
    import ResetUserItem from "@/components/reset/ResetUserItem.vue";
    const d = require('lodash').debounce;

    @Component({
        components: {
            ResetUserItem
        }
    })
    export default class Reset extends Mixins<IBaseComponent>(BaseComponent){
        private searchText: string = '';

        private loading: boolean = false;

        private users: IUser[] = []

        private debounced: Function = d(this.searchUsers, 250);

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
            this.users = [];
        }

        @Watch('searchText', {immediate: true})
        private handleSearchUsers() {
            this.debounced();
        }
    }
</script>

<style scoped lang="less">
.reset-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    .search-user {
        width: 80%;
        margin-top: 50px;
    }
    .found-users {
        margin: 50px auto;
        width: 80%;
        align-self: flex-start;
    }
}

</style>