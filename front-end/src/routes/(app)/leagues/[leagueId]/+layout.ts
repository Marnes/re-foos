import type { LayoutLoad } from './$types';
import { league, players } from '$src/stores/leagueStore';
import { seasonPath } from '$src/lib/util/match/leagueUtil';

export const load: LayoutLoad = async ({ fetch, params, url }) => {
    const season = url.searchParams.get('season');
    const leagueId = params.leagueId;

    let leagueResponse = await fetch(seasonPath(`/leagues/${ leagueId }`, season))
    league.set(await leagueResponse.json());

    let playersResponse = await fetch(`/leagues/${ leagueId }/players`);
    players.set(await playersResponse.json());

    return {};
}


