<template>
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="120px" class="login-form" v-loading.lock="isLoading">
        <el-form-item label="Логин" prop="login">
            <el-input v-model="ruleForm.login" autocomplete="off" autofocus></el-input>
        </el-form-item>
        <el-form-item label="Пароль" prop="pass">
            <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">Войти</el-button>
        </el-form-item>

        <p>Нет аккаунта?
            <span>
            <router-link to="/signup">Зарегистрироваться</router-link>
        </span>
        </p>
    </el-form>
</template>

<script lang="ts">
    import {Component, Mixins} from 'vue-property-decorator';
    import validatePass from "@/common/validators/passwordValidator";
    import notEmptyFieldValidator from "@/common/validators/notEmptyFieldValidator";
    import AppModule from "@/store/modules/app/AppModule";
    import {AxiosError} from "axios";
    import BaseComponent from "@/components/BaseComponent.vue";
    import ROUTES from "@/common/constants/ROUTES";
    import {IBaseComponent, ILoginResponse} from "@/common/types/types";

    @Component
    export default class Login extends Mixins<IBaseComponent>(BaseComponent) {
        private ruleForm: LoginRuleForm = {
            login: '', pass: ''
        };

        private rules: Object = {
            pass: [
                {validator: notEmptyFieldValidator, trigger: 'submit'},
                {validator: validatePass, trigger: 'submit'},
            ],
            login: [
                {validator: notEmptyFieldValidator, trigger: 'submit'},
            ]
        };

        public submitForm(formName: string) {
            //@ts-ignore
            this.$refs[formName].validate((valid: boolean) => {
                if (!valid || AppModule.isLoading) {
                    return;
                }
                AppModule.setIsLoading(true)
                this.api.auth.login(this.ruleForm.login, this.ruleForm.pass)
                    .then((response: ILoginResponse) => {
                        AppModule.setUserInfo({
                            email: '',
                            nickname: this.ruleForm.login,
                            authKey: response.authToken
                        });

                        for (let prop in this.ruleForm) {
                            // @ts-ignore
                            this.ruleForm[prop] = '';
                        }

                        this.$router.push(ROUTES.HOME);
                    })
                    .catch((err: AxiosError) => {
                        switch (err.response?.status) {
                            case 404:
                                this.showErrorNotify({message: 'Пользователь не найден', title: 'Ошибка'});
                                break;
                            case 400:
                                this.showErrorNotify({title: 'Ошибка', message: 'Неверный пароль'});
                                break;
                            default:
                                this.showErrorNotify();
                        }
                    })
                    .finally(() => AppModule.setIsLoading(false));
            });
        };
    }

    type LoginRuleForm = {
        login: string, pass: string
    }
</script>

<style scoped lang="less">
    .login-form {
        width: 400px;
        margin: 200px auto;

        /deep/ button {
            float: right;
        }

        p {
            display: inline-block;
            width: max-content;
            float: right;

            span {
                color: #409EFF;
                cursor: pointer;

                a {
                    text-decoration: none;
                    color: inherit;
                }
            }
        }
    }


    @media screen and (max-width: 550px) {
        .login-form {
            margin-top: 100px;
            margin-left: -25px;
            display: flex;
            flex-direction: column;
            transform: scale(.8);
            .el-form-item {
                width: 80%;
            }

            p {
                display: flex;
                align-self: center;

                span {
                    margin-left: 2px;
                }
            }
        }
    }
</style>