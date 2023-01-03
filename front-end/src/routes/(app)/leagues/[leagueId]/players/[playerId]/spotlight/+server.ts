import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';
import { seasonPath } from '$src/lib/util/match/leagueUtil';

export const GET: RequestHandler = async ({ url, params }) => {
    const season = url.searchParams.get('season');
    const response = await api.get(seasonPath(`/leagues/${ params.leagueId }/players/${ params.playerId }/spotlight`, season));
    return json(await response.json());
}
