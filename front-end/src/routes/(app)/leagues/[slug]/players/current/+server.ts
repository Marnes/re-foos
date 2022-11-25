import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const GET: RequestHandler = async ({ params, locals }) => {
    let response = await api.get(`/leagues/${ params.slug }/players/current`, locals.session.jwt);

    return json(await response.json());
}
