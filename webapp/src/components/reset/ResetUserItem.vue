<template>
    <div style="display: flex;">
        <h1>{{user.nickname}}</h1>
        <el-popconfirm
                confirmButtonText='Да'
                cancelButtonText='Нет'
                @onConfirm="handleResetPassword"
                title="Сбросить пароль?">
            <el-button slot="reference" type="primary" icon="el-icon-refresh-left">Сбросить</el-button>
        </el-popconfirm>
    </div>
</template>

<script lang="ts">
    import {Vue, Component, Prop} from "vue-property-decorator";
    import IUser from "../../common/types/IUser";
    import AppModule from "@/store/modules/app/AppModule";

    @Component
    export default class ResetUserItem extends Vue{
        @Prop({type: Object, required: true})
        private user!: IUser;

        handleResetPassword() {
            AppModule.resetPassword(this.user.id)
            console.log(this.user.id)
        }
    }
</script>

<style scoped>
h1  {
    display: block;
}
button {
    margin-left: 30px;
    height: 40px;
    margin-top: 20px;
}
</style>