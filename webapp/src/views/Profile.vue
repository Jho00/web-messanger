<template>
    <div>
        <div v-show="!isError" v-loading.lock="loading">
            <div class="action-button">
                <el-button type="primary"
                           size="small"
                           @click="handleStartMessaging"
                           v-loading.lock="loadingMessageAction">
                    Написать сообщение
                </el-button>
                <el-button
                        :type="friendButtonType"
                        @click="handleFriendButtonClick"
                        :disabled="user.sentRequest"
                        v-loading.lock="loadingFriendAction"
                        size="small">
                    {{friendButtonText}}
                </el-button>
            </div>

            <div class="main-user-info">
                <el-input readonly v-model="user.nickname" class="info-item">
                    <template slot="prepend"><strong class="main-prepend">Обращение</strong></template>
                </el-input>

                <el-input readonly v-model="user.email" class="info-item">
                    <template slot="prepend"><strong class="main-prepend">Почта</strong></template>
                </el-input>
            </div>

            <div class="user-info">
                <el-input readonly v-model="fullName" class="info-item">
                    <template slot="prepend">Полное имя</template>
                </el-input>

                <el-input readonly v-model="user.phone" class="info-item">
                    <template slot="prepend">Телефон</template>
                </el-input>

                <el-input readonly v-model="user.address" class="info-item">
                    <template slot="prepend">Домашний адрес</template>
                </el-input>

                <el-input readonly v-model="user.bio" class="info-item">
                    <template slot="prepend">О себе</template>
                </el-input>
            </div>
        </div>
        <div v-show="isError" class="error-msg">
            Такого пользователя не сущестует
            <el-button type="primary" @click="goBack">Вернуться к общению</el-button>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Mixins, Prop} from "vue-property-decorator";
    import {IBaseComponent, IStartDialogMixin, SettingsForm} from "@/common/types/types";
    import BaseComponent from "@/components/BaseComponent.vue";
    import ROUTES from "@/common/constants/ROUTES";
    import ThreadsModule from "@/store/modules/threads/ThreadsModule";
    import IThread from "@/common/types/IThread";
    import AppModule from "@/store/modules/app/AppModule";
    import {initialSettingsForm} from "@/common/common";
    import StartDialogMixin from "@/components/common/StartDialogMixin.vue";

    @Component
    export default class Profile extends Mixins<IBaseComponent, IStartDialogMixin>(BaseComponent, StartDialogMixin) {
        @Prop({type: String, required: true})
        private username!: string;

        private loading: boolean = true;
        private isError: boolean = false;

        private loadingFriendAction: boolean = false;

        private user: SettingsForm = initialSettingsForm;

        mounted() {
            if (this.username === AppModule.nickname) {
                this.$router.push(ROUTES.ME);
                return;
            }

            ThreadsModule.fetchThreads();
            this.getTargetProfile();
            document.addEventListener('updateFriend', this.getTargetProfile);
        }

        beforeDestroy() {
            document.removeEventListener('updateFriend', this.getTargetProfile, true);
        }
        async getTargetProfile() {
            this.isError = false;
            this.loading = true;
            try {
                const result = await this.api.users.getProfile(this.username);
                this.user = result.data;
            } catch (e) {
                if (e.response.status === 403) {
                    this.$router.push(ROUTES.ME);
                    return;
                }
                this.isError = true;
            } finally {
                this.loading = false;
            }
        }

        goBack() {
            this.$router.push(ROUTES.THREADS);
        }

        async handleFriendButtonClick() {
            if (this.user.sentRequest) {
                return false;
            }
            const apiCall = this.user.friended ? this.api.friends.deleteFriend : this.api.friends.add;
            this.loadingFriendAction = true;
            try {
                const result = await apiCall(this.user.id);
                if (!result) {
                    this.showErrorNotify();
                } else  {
                    this.updateModelAfterSuccessFriendRequest();
                }
            } catch (e) {
                console.log(e);
                this.showErrorNotify();
            } finally {
                this.loadingFriendAction = false;
            }
        }

        updateModelAfterSuccessFriendRequest() {
            if (!this.user.friended) {
                this.user.sentRequest = true;
            } else {
                this.user.sentRequest = false;
                this.user.friended = false;
            }
        }

        get fullName() {
            if (!this.user.firstName && !this.user.lastName) {
                return `Полное имя не задано`;
            }

            return `${this.user.firstName} ${this.user.lastName}`
        }

        get friendButtonText() {
            if (this.user.sentRequest) {
                return 'Запрос отправлен';
            }
            return this.user.friended ? 'Удалить из друзей' : 'Добавить в друзья';
        }

        get friendButtonType() {
            if (this.user.sentRequest) {
                return 'warning';
            }
            return this.user.friended ? 'danger' : 'success';
        }
    }
</script>

<style scoped lang="less">
    .action-button {
        margin-left: 10px;
    }

    .main-prepend {
        font-size: 1.1em;
        text-effect: emboss;
    }

    .error-msg {
        display: flex;
        flex-direction: column;
        width: 320px;
        align-items: center;
        margin: auto;

        button {
            margin-top: 10px;
        }
    }

    .full-name-empty {
        color: darkred;
    }

    .user-info {
        width: 70%;
        margin: auto;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        .info-item {
            margin-top: 10px;
        }
    }

    .main-user-info {
        width: 320px;
        margin: auto;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        .info-item {
            margin-top: 10px;
        }
    }

    @media screen and (max-width: 550px) {
        .user-info {
            width: 100%;
            box-sizing: border-box;
            padding: 10px;
        }
        .action-button {
            display: flex;
            margin: auto;
            justify-content: center;
        }
    }
</style>