import IUser from "@/common/types/IUser";

class User implements IUser{
    nickname: string;
    email: string;
    id: number;

    constructor(data: IUser) {
        this.nickname = data.nickname;
        this.email = data.email;
        this.id = data.id;
    }
}

export default User;