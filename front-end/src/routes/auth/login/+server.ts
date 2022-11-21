import api from '$src/lib/api';
import { invalid, error, json } from '@sveltejs/kit';
import { getExpiryDate, parseJwt } from '$src/lib/util/jwt-util';
import type { RequestHandler } from '@sveltejs/kit';
import { isSuccess } from "$src/lib/utils";

export const POST: RequestHandler = async ({ cookies, request }) => {
    const user = await request.json();

    const response = await api.post('/auth/login', user);

    if (!isSuccess(response.status)) {
        throw error(response.status);
    }

    const userJson = await response.json();

    cookies.set('jwt', userJson.jwt, {
        path: '/',
        httpOnly: true,
        sameSite: 'strict',
        expires: getExpiryDate(parseJwt(userJson.jwt)!.exp)
    });

    return json(userJson);
}

