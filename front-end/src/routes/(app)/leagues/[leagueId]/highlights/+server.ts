import { json, type RequestHandler } from '@sveltejs/kit';
import api from '$src/lib/api';

export const GET: RequestHandler = async ({ params }) => {
    let response = await api.get(`/leagues/${ params.leagueId }/highlights`);

    return json(await response.json());
}
