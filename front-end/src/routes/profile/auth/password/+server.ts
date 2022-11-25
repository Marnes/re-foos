import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { error, json } from '@sveltejs/kit';
import { isSuccess } from '$src/lib/utils';

export const PUT: RequestHandler = async ({ request, locals }) => {
    const passwordChange = await request.json();
    const response = await api.post('/auth/change-password', passwordChange, locals.session.jwt);

    if (!isSuccess(response.status)) {
        throw error(401);
    }

    return json({});
}


