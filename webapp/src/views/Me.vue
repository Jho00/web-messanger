<template>
    <el-form :model="settings" :rules="rules" ref="settings" class="settings-form" v-loading.lock="loading" element-loading-text="Обновление...">
        <div class="for-bluring" :class="{blured: loading}">
            <el-form-item label="Никнейм" prop="nickname">
                <el-input v-model="settings.nickname"></el-input>
            </el-form-item>
            <el-form-item label="Электронная почта" prop="email">
                <el-input v-model="settings.email"></el-input>
            </el-form-item>
            <el-form-item label="Мобильный телефон" prop="phone">
                <el-input v-model="settings.phone"></el-input>
            </el-form-item>
            <el-form-item label="Имя" prop="firstName">
                <el-input v-model="settings.firstName"></el-input>
            </el-form-item>
            <el-form-item label="Фамилия" prop="lastName">
                <el-input v-model="settings.lastName"></el-input>
            </el-form-item>
            <el-form-item label="Адрес" prop="address">
                <el-input type="textarea" v-model="settings.address"></el-input>
            </el-form-item>
            <el-form-item label="Кратко о себе" prop="bio">
                <el-input type="textarea" v-model="settings.bio"></el-input>
            </el-form-item>
        </div>
        <el-form-item>
            <el-button class="save-button" :type="saveButtonType" :disabled="loading" @click="submitForm('settings')" >Сохранить</el-button>
            <el-button :disabled="loading" @click="resetForm('settings')">Очистить поля</el-button>
        </el-form-item>
    </el-form>
</template>

<script lang="ts">
import {Component, Mixins} from 'vue-property-decorator';
import {IBaseComponent, SettingsForm} from "@/common/types/types";
import BaseComponent from "@/components/BaseComponent.vue";
import isEmailValidator from "@/common/validators/isEmailValidator";
import isPhoneValidator from "@/common/validators/isPhoneValidator";
import AppModule from "@/store/modules/app/AppModule";
import {initialSettingsForm} from "@/common/common";

@Component
export default class Me extends Mixins<IBaseComponent>(BaseComponent) {
    private saveButtonType: string = 'primary';

    private settings: SettingsForm = initialSettingsForm;

    private loading: boolean = false;

    private  rules: Object =  {
        nickname: [
            { required: true, message: 'Имя пользователя обязательно', trigger: 'submit' },
        ],
        email: [
            { required: true, message: 'Электронная почта обязательна', trigger: 'submit' },
            { validator: isEmailValidator, trigger: 'submit' },
        ],
        phone: [
            { validator: isPhoneValidator, trigger: 'submit' },
        ],
    }

     submitForm(formName: string) {
        //@ts-ignore
        this.$refs[formName].validate((valid:boolean) => {
            if (!valid) {
                return;
            }

            this.updateSettings();
        });

    }

    resetForm(formName: string) {
        // @ts-ignore
        this.$refs[formName].resetFields();
        setTimeout(() => {this.saveButtonType = 'success'}, 300)
        setTimeout(() => {this.saveButtonType = 'primary'}, 1600)
    }

    async mounted() {
        if (!AppModule.settings) {
            await AppModule.fetchSettings();
        }
        this.init();
    }

    private init() {
        if (AppModule.settings === null) {
            console.error('Settings is empty')
        }
        // @ts-ignore
        this.settings = AppModule.settings
    }

    private async updateSettings() {
        this.loading = true;
        try {
            const result = await this.api.users.setSettings(this.settings);
            this.showSuccessNotify();
            AppModule.updateSettings(this.settings);
            this.init();
        } catch (e) {
            const status = e.response.status;
            if (status === 400) {
                this.showErrorNotify( {
                    title: "Не удалось сохранить",
                    message: "Пользователь с такими данными уже существует"
                })
            } else {
                this.showErrorNotify();
            }
        } finally {
            this.loading = false;
        }
    }
}
</script>

<style scoped lang="less">
    .blured {
        filter: blur(1.5px);
    }

    .save-button {
        transition: .7s all;
    }

    /deep/.el-loading-mask {
        background-color: transparent;
    }
    .settings-form {
        width: 90%;

        .for-bluring {
            transition: all 1.5s;
        }

        /deep/.el-form-item__label {
            width: 200px;
        }

        /deep/.el-form-item__content {
            margin-left: 200px;
        }
    }

    @media screen and (max-width: 550px) {
        .el-form-item {
            display: flex;
            flex-direction: column;

            /deep/.el-form-item__label {
                margin-left: 10px;
                text-align: left;
                width: 300px;
            }
            /deep/.el-form-item__content {
                margin-left: 10%;
            }
        }
    }
</style>