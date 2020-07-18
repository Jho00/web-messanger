<template>
    <div class="friends-wrapper">
        <friend-item v-for="friend in friends" :key="friend.id" :user="friend"/>
        <h2 class="friends-empty-state" v-show="friends.length === 0">
            У вас пока нет друзей
        </h2>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import FriendsModule from "@/store/modules/friends/FriendsModule";
import FriendItem from "@/components/friends/FriendItem.vue";

@Component({
    components: {FriendItem}
})
export default class FriendsTab extends Vue {
    get friends() {
        return FriendsModule.allFriends;
    }
}
</script>

<style scoped lang="less">
    .friends-wrapper {
        .friends-empty-state {
            text-align: center;
        }
    }
</style>