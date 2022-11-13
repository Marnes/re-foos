import jwt_decode from 'jwt-decode';
import type { User } from '$src/models/user';
import _ from 'lodash';
import moment from 'moment';

export const parseJwt = (jwt: string): User | null => {
    if (_.isEmpty(jwt)) {
        return null;
    }

    return jwt_decode(jwt);
}

export const isExpired = (exp: number): boolean => {
    return moment().isAfter(moment.unix(exp));
}

export const getExpiryDate = (exp: number): Date => {
    return moment.unix(exp).toDate();
}
