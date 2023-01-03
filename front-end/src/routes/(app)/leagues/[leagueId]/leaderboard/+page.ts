import type { PageLoad } from './$types';
import { highlights } from '$src/stores/leagueStore';
import { seasonPath } from '$src/lib/util/match/leagueUtil';

export const load: PageLoad = async ({ fetch, params, url }) => {
    const season = url.searchParams.get('season');

    let highlightsResponse = await fetch(seasonPath(`/leagues/${ params.leagueId }/highlights`, season));
    highlights.set(await highlightsResponse.json());

    let leaderboardResponse = await fetch(seasonPath(`/leagues/${ params.leagueId }/leaderboard`, season));

    return { leaderboard: await leaderboardResponse.json() };
}


