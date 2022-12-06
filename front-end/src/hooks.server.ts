import api from '$src/lib/api';
import { isExpired, JWT_KEY } from '$src/lib/util/jwt-util';
import { sequence } from '@sveltejs/kit/hooks';
import type { Handle } from '@sveltejs/kit';

const userSessionHandler = async ({ event, resolve }: any) => {
    const jwt = event.cookies.get(JWT_KEY);


    if (jwt && !isExpired(jwt)) {
        let response = await api.get(`/users/current`, jwt);
        event.locals.session = { user: await response.json(), jwt };
    } else {
        event.cookies.delete(JWT_KEY, { path: '/' });
    }

    return await resolve(event);
}

export const handle: Handle = sequence(userSessionHandler);


