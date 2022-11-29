import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const GET: RequestHandler = async ({ url, params }) => {
    const response = await api.get(`/leagues/${ params.leagueId }/players/${ params.playerId }/spotlight`);
    return json(await response.json());
}
