<template>
    <div class="friend-panel">
        <el-tabs v-model="activeTab" stretch >
            <el-tab-pane :label="labelFriends" name="first">
                <friends-tab/>
            </el-tab-pane>
            <el-tab-pane :label="labelRequests" name="second">
                <requests-tab/>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script lang="ts">
import {Component, Vue} from "vue-property-decorator";
import FriendsTab from "@/components/friends/FriendsTab.vue";
import RequestsTab from "@/components/friends/RequestsTab.vue";
import FriendsModule from "@/store/modules/friends/FriendsModule";


@Component({
    components: {FriendsTab, RequestsTab}
})
export default class Friends extends Vue{
    private activeTab: string = 'first'

    mounted() {
        FriendsModule.fetch();
    }

    get labelFriends() {
        if (FriendsModule.allFriends.length === 0) {
            return 'Друзья';
        }
        return `Друзья : ${FriendsModule.allFriends.length}`
    }

    get labelRequests() {
        if (FriendsModule.requests.length === 0) {
            return 'Заявки';
        }
        return `Заявки : ${FriendsModule.requests.length}`
    }
}
</script>

<style scoped lang="less">
.friend-panel {
    width: 70%;
    margin: auto;
}

@media screen and (max-width: 550px) {
    .friend-panel {
        width: 100%;
    }
}
</style>