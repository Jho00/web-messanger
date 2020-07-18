<template>
    
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import AppModule from "@/store/modules/app/AppModule";
    import {ApiStructure, IBaseComponent, NotifyData} from "@/common/types/types";
    import api from "@/api/api";

    @Component
    export default class BaseComponent extends Vue implements IBaseComponent{
        public showErrorNotify(data: NotifyData = {message: 'Что-то пошло не так', title: 'Ошибка'}) {
            this.$notify.error(data);
        }

        public showWarningNotify(data: NotifyData = {message: 'Что-то пошло не так', title: 'Ошибка'}) {
            this.$notify.warning(data);
        }

        public showInfoNotify(data: NotifyData = {message: '', title: 'Информация'}) {
            this.$notify.info(data);
        }

        public showSuccessNotify(data: NotifyData = {message: '', title: 'Успешно'}) {
            this.$notify.success(data);
        }

        get isLoading() {
            return AppModule.isLoading;
        }

        api: ApiStructure = api;
    }



</script>

<style scoped>

</style>