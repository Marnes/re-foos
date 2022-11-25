import moment from 'moment';
import jwt_decode from 'jwt-decode';

export const JWT_KEY = 'jwt';

const decodeJwt = (jwt: string): any => {
    return jwt_decode(jwt)
}

export const isExpired = (jwt: string): boolean => {
    return moment().isAfter(moment.unix(decodeJwt(jwt).exp));
}

export const getExpiryDate = (jwt: string): Date => {
    return moment.unix(decodeJwt(jwt).exp).toDate();
}
