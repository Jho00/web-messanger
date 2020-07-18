<template>
    <transition-group name="list">
        <slot/>
    </transition-group>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";

    @Component
    export default class ListTransition extends Vue{}
</script>

<style scoped lang="less">
    /deep/.list-enter-active, /deep/.list-leave-active {
        transition: all .5s ease-in;
    }
    /deep/.list-enter, /deep/.list-leave-to /* .list-leave-active below version 2.1.8 */ {
        opacity: 0;
        transform: translateY(30px);
    }
</style>