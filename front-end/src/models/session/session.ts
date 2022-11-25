import type { User } from '$src/models/user';

export class Session {
    constructor(user: User, jwt: string) {
        this.user = user;
        this.jwt = jwt;
    }

    user: User;
    jwt: string
}
