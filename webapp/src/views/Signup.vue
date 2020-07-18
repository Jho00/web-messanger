<template>
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="120px" class="login-form" v-loading.lock="isLoading">
        <el-form-item label="Логин" prop="login">
            <el-input v-model="ruleForm.login" autocomplete="off" autofocus></el-input>
        </el-form-item>
        <el-form-item type="email" label="Email" prop="email">
            <el-input v-model="ruleForm.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Пароль" prop="pass">
            <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Пароль снова" prop="passRepeat">
            <el-input type="password" v-model="ruleForm.passRepeat" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">Зарегестрироваться</el-button>
        </el-form-item>

        <p>Уже есть аккаунт?
            <span>
                <router-link to="/login">Войти</router-link>
            </span>
        </p>
    </el-form>
</template>

<script lang="ts">
    import {Component, Mixins} from "vue-property-decorator";
    import notEmptyFieldValidator from "@/common/validators/notEmptyFieldValidator";
    import validatePass from "@/common/validators/passwordValidator";
    import BaseComponent from "@/components/BaseComponent.vue";
    import AppModule from "@/store/modules/app/AppModule";
    import {AxiosError} from "axios";
    import ROUTES from "@/common/constants/ROUTES";
    import {IBaseComponent, ILoginResponse} from "@/common/types/types";
    import isEmailValidator from "@/common/validators/isEmailValidator";

    @Component
    export default class Signup extends Mixins<IBaseComponent>(BaseComponent){
        private ruleForm: IForm = {
            login: '', pass: '', email: '', passRepeat: ''
        };

        private rules: Object = {
            pass: [
                {validator: notEmptyFieldValidator, trigger: 'submit'},
                {validator: validatePass, trigger: 'submit'},
            ],
            passRepeat: [
                {validator: notEmptyFieldValidator, trigger: 'submit'},
                {validator: (rule: any, value: string, callback: Function) => {
                        if (value !== this.ruleForm.pass) {
                            callback(new Error('Пароли должны совпадать'));
                        } else {
                            callback();
                        }
                    }, trigger: 'submit'},
            ],
            login: [
                {validator: notEmptyFieldValidator, trigger: 'submit'},
            ],
            email: [
                {validator: notEmptyFieldValidator, trigger: 'submit'},
                {validator: isEmailValidator, trigger: 'submit'},
            ]
        };

        public submitForm(formName: string) {
            //@ts-ignore
            this.$refs[formName].validate((valid: boolean) => {
                if (!valid || AppModule.isLoading) {
                    return;
                }
                AppModule.setIsLoading(true);
                this.api.auth.signup(this.ruleForm.login, this.ruleForm.email, this.ruleForm.pass, this.ruleForm.passRepeat)
                    .then((response: ILoginResponse) => {

                        AppModule.setUserInfo({
                            email: this.ruleForm.email,
                            nickname: this.ruleForm.login,
                            authKey: response.authToken
                        });

                        for(let prop in this.ruleForm) {
                            // @ts-ignore
                            this.ruleForm[prop] = '';
                        }

                        this.$router.push(ROUTES.HOME);
                    })
                    .catch((err: AxiosError) => {
                        if (err.response?.status === 400) {
                            this.showErrorNotify({title: 'Ошибка', message: err.response?.data?.authToken});
                        } else {
                            this.showErrorNotify();
                        }
                    })
                    .finally(() => AppModule.setIsLoading(false));
            });
        };
    }

    type IForm = {
        login: string,
        pass: string,
        email: string,
        passRepeat: string
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