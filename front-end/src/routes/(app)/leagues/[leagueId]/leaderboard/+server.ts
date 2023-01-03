import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';
import { seasonPath } from '$src/lib/util/match/leagueUtil';

export const GET: RequestHandler = async ({ params, url }) => {
    const season = url.searchParams.get('season');

    let response = await api.get(seasonPath(`/leagues/${ params.leagueId }/leaderboard`, season));

    return json(await response.json());
}
