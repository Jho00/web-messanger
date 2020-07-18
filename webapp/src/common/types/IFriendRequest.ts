export default interface IFriendRequest {
    id: number,
    ownerId: number,
    targetId: number,
    ownerNickname: string,
    targetNickname: string
}