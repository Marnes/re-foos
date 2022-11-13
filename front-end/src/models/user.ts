export class User {
	username!: string;
	avatar!: string;
	exp!: number;

	static fromJwt(jwt: any): User {
		const user = new User();

		return user;
	}
}
