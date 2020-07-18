<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <el-switch v-model="value1">
    </el-switch>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component
export default class HelloWorld extends Vue {
  @Prop({default: 'Example IMessage'}) private msg!: string;

  private value1: boolean = false;
}
</script>

<style scoped lang="less">

</style>
