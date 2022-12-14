import { env } from '$env/dynamic/public'
import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { error, json } from '@sveltejs/kit';
import { getExpiryDate, JWT_KEY } from '$src/lib/util/jwt-util';
import { isSuccess } from '$src/lib/utils';

export const POST: RequestHandler = async ({ cookies, request }) => {
    const user = await request.json();

    const response = await api.post('/auth/login', user);

    if (!isSuccess(response.status)) {
        throw error(response.status);
    }

    const userJson = await response.json();

    cookies.set(JWT_KEY, userJson.jwt, {
        domain: env.PUBLIC_DOMAIN,
        path: '/',
        httpOnly: true,
        expires: getExpiryDate(userJson.jwt),
        secure: env.PUBLIC_ENV === 'PROD'
    });

    return json(userJson);
}

export const DELETE: RequestHandler = async ({ cookies }) => {
    await cookies.delete(JWT_KEY, { path: '/' });
    return json({})
}

