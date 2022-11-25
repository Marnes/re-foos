import { humanDate } from '$src/lib/util/date-util';

export class User {
    id!: number;
    username!: string;
    avatar!: string;
    createdDate!: Date;
}
