<template>
    <div>
        <el-card v-show="requests.length > 0" class="request-card" shadow="hover">
            <list-transition>
                <div v-for="request in requests" :key="request.id" class="request-item">
                    <router-link :to="`profile/${request.ownerNickname}`">
                        {{request.ownerNickname}}
                    </router-link>
                    <div class="actions">
                        <el-button @click="handleAccept(request.id)" type="primary" icon="el-icon-check" round/>
                        <el-button @click="handleDecline(request.id)" type="warning" icon="el-icon-close" round/>
                    </div>
                </div>
            </list-transition>
        </el-card>
        <h2 v-show="requests.length === 0">
            Заявок в друзья нет
        </h2>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import ListTransition from "@/components/common/ListTransition.vue"
    import FriendsModule from "@/store/modules/friends/FriendsModule";

    @Component({
        components: {ListTransition}
    })
    export default class RequestsTab extends Vue{
        get requests() {
            return FriendsModule.friendRequests;
        }

        handleAccept(id: number) {
            FriendsModule.acceptRequest(id);
        }
        handleDecline(id: number) {
            FriendsModule.declineRequest(id);
        }
    }
</script>

<style scoped lang="less">
h2 {
    text-align: center;
}
.request-card {
    width: 100%;
    .request-item {
        width: 100%;
        height: 70px;
        display: flex;
        align-items: center;
        opacity: .6;
        position: relative;
        &:hover {
            opacity: 1;
        }
        a {
            text-decoration: none;
            font-size: 1.4em;
            color: black;
            transition: .3s all;
            &:hover {
                color: blue;
            }
        }
        .actions {
            position: absolute;
            right: 0;
        }
    }
}

@media screen and (max-width: 550px) {
    .request-card {
        border-top: none;
        .request-item {
            opacity: 1 !important;
        }
    }
}
</style>