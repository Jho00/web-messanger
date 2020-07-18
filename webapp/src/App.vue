<template>
  <div id="app">
    <top-menu v-show="isLogined"></top-menu>
    <main>
      <router-view></router-view>
    </main>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import TopMenu from './components/TopMenu.vue';
import AppModule from "@/store/modules/app/AppModule";
import ThreadsModule from "@/store/modules/threads/ThreadsModule";
import Ws from "@/common/Ws";

@Component({
  components: {
    TopMenu
  },
})
export default class App extends Vue {
  get isLogined(): boolean {
    return AppModule.isAuthenticated;
  }

  mounted() {
    AppModule.isLogined();
  }
}
</script>

<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

body {
  margin: 0;
}
main {
  margin-top: 60px;
}

.el-notification {
  margin-top: 50px;
}
</style>
