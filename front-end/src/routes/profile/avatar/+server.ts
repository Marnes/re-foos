import { json, type RequestHandler } from '@sveltejs/kit';
import api from '$src/lib/api';

export const GET: RequestHandler = async ({ locals }) => {
    let response = await api.get('/profile/avatars', locals.session.jwt);
    return json(await response.json());
}

export const PUT: RequestHandler = async ({ request, locals }) => {
    const avatar: String = await request.json();
    await api.put('/profile/avatars', avatar, locals.session.jwt);

    return json({});
}
