import api from '$src/lib/api';
import type { RequestEvent } from '@sveltejs/kit';
import { invalid } from '@sveltejs/kit';
import { getExpiryDate, parseJwt } from '$src/lib/util/jwt-util';

/** @type {import('./$types').Actions} */
export const actions = {
    login: async ({ request, cookies }: RequestEvent) => {
        const user = await request.formData();

        const response = await api.post('auth/login', {
            username: user.get('username'),
            password: user.get('password')
        });

        if (response.status === 200) {
            const userJson = await response.json();

            cookies.set('jwt', userJson.jwt, {
                path: '/',
                httpOnly: true,
                sameSite: 'strict',
                expires: getExpiryDate(parseJwt(userJson.jwt)!.exp)
            });

            return { user: userJson };
        } else {
            return invalid(401);
        }
    },
    logout: async ({ cookies }: RequestEvent) => {
        await cookies.delete("jwt", { path: '/' });
        return {}
    }
}
