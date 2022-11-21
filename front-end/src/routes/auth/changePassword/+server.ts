import api from '$src/lib/api';
import { error, json } from '@sveltejs/kit';
import type { RequestHandler } from '@sveltejs/kit';
import { isSuccess } from "$src/lib/utils";

export const POST: RequestHandler = async ({ cookies, request }) => {
    const passwordChange = await request.json();
    const response = await api.post('/auth/change-password', passwordChange, cookies.get('jwt'));

    if (!isSuccess(response.status)) {
        return error(401);
    }

    // await cookies.delete("jwt", { path: '/' });
    return json({});
}


