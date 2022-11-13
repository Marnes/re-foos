import { isExpired, parseJwt } from '$src/lib/util/jwt-util';
import _ from 'lodash';
import type { Handle } from "@sveltejs/kit";

/** @type {import('@sveltejs/kit').Handle} */
export async function handle({ event, resolve }) {
    const jwt = event.cookies.get('jwt');

    if (!_.isEmpty(jwt) && !isExpired(jwt.exp)) {
        event.locals.jwt = jwt;
        event.locals.user = parseJwt(jwt);
    } else if (!_.isEmpty(jwt)) {
        event.cookies.delete('jwt');
    }

    return await resolve(event);
}
