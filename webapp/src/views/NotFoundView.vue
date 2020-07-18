<template>
    <section class="page_404">
        <div class="container">
            <div class= "text-center">
                <br>
                <div class="four_zero_four_bg">
                    <h1 class="text-center" >Страница не найдена</h1>
                </div>

                <div class="contant_box_404">
                    <el-button type="primary" @click="goToThreads" >Продолжить общаться</el-button>
                </div>
            </div>
        </div>
    </section>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import ROUTES from "@/common/constants/ROUTES";

@Component
export default class NotFoundView extends Vue {
    goToThreads() {
        this.$router.push(ROUTES.THREADS);
    }
}
</script>

<style scoped lang="less">
    .page_404 {
        padding: 40px 0;
        background: #fff;
        font-family: 'Arvo', serif;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .page_404 img {
        width: 100%;
    }
    .four_zero_four_bg {
        height: 400px;
        width: 100%;
        background: url('../assets/dribbble_1.gif') no-repeat center;
    }
    .four_zero_four_bg h1 {
        font-size: 3em;
    }
    .four_zero_four_bg h3 {
        font-size: 80px;
    }
    .link_404 {
        color: #fff !important;
        background: #39ac31;
        margin: 20px 0;
        display: inline-block;
    }
    .contant_box_404 {
        margin-top: -50px;
    }
    .text-center {
        text-align: center;
    }
</style>