import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const GET: RequestHandler = async ({ url, params }) => {
    let response = await api.get(`/leagues/${ params.slug }/players/spotlight`);

    return json(await response.json());
}
