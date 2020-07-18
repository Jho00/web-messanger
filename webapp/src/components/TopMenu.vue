<template>
    <header>
        <el-link class="my-link" :underline="false" @click="goTo('/')">
            <i class="el-icon-s-promotion"></i> Общение
        </el-link>
        <el-link class="my-link" :class="{'blink-item': hasRequests}" :underline="false" @click="goTo('/friends')">
            <i class="el-icon-user-solid"></i> {{labelFriends}}
        </el-link>
        <el-link class="my-link" :underline="false" @click="goTo('/ME')">
            <i class="el-icon-setting"></i> Обо мне
        </el-link>
        <el-link v-if="isAdmin" class="my-link" :underline="false" @click="goTo('/reset')">
            <i class="el-icon-refresh"></i> <span class="refresh-passwords">Сбросить пароли</span>
        </el-link>

        <el-link class="logout-button" :underline="false" @click="logout">
          <i class="el-icon-close"></i>  Выйти
        </el-link>
    </header>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import Logo from '@/components/top-menu/Logo.vue';
    import AppModule from "@/store/modules/app/AppModule";
    import FriendsModule from "@/store/modules/friends/FriendsModule";
    import ROUTES from "@/common/constants/ROUTES";
    import ThreadsModule from "@/store/modules/threads/ThreadsModule";

    @Component({
        components: {
            Logo
        }
    })
    export default class TopMenu extends Vue {
        async logout() {
          await AppModule.logout();
          window.location.reload();
        }

        goTo(route: string) {
            if (route === ROUTES.HOME && !!ThreadsModule.activeThread) {
                ThreadsModule.setThread(-1);
            }
            this.$router.push(route);
        }

        get labelFriends() {
            if (FriendsModule.requests.length === 0) {
                return 'Друзья';
            }
            return `Друзья(${FriendsModule.requests.length})`
        }

        get hasRequests() {
            return FriendsModule.requests.length > 0;
        }

        get isAdmin() {
            return AppModule?.settings?.isAdmin;
        }
    }
</script>

<style scoped lang="less">
  header {
      width: 100%;
      height: 50px;
      display: flex;
      position: fixed;
      top: 0;
      left: 0;
      z-index: 10000000;
      background: white;

      .logo {
          margin-left: 10px;
      }
      .my-link {
          margin-left: 10px;
          user-select: none;
      }

      .profile {
          position: absolute;
          right: 60px;
          top: 10px;
          cursor: pointer;
      }

      .logout-button {
          position: absolute;
          top: 17px;
          right: 10px;
      }
  }

  .blink-item {
      animation: blink-item-animation 3s linear infinite;
      :hover {
          animation: none;
      }
  }
  @keyframes blink-item-animation {
      0%   {
          color: rgba(255, 0, 0, 0.5)
      }
      50%  {color: black}
      100% {
          color: rgba(255, 0, 0, 0.5)
      }
  }

  @media screen and (max-width: 550px) {
      .refresh-passwords {
          display: none;
      }
  }
</style>