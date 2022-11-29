import { PUBLIC_DOMAIN, PUBLIC_ENV } from '$env/static/public'
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
        domain: PUBLIC_DOMAIN,
        path: '/',
        httpOnly: true,
        expires: getExpiryDate(userJson.jwt),
        secure: PUBLIC_ENV === 'PROD'
    });

    return json(userJson);
}

export const DELETE: RequestHandler = async ({ cookies }) => {
    await cookies.delete("jwt", { path: '/' });
    return json({})
}

