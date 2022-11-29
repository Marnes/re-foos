import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const PUT: RequestHandler = async ({ params }) => {
    let response = await api.put(`/leagues/${ params.leagueId }/players/spotlight`, {});
    return json(await response.json());
}
