import type { LayoutLoad } from './$types';
import { league, players, playerSpotlight } from '$src/stores/leagueStore';

export const load: LayoutLoad = async ({ fetch, params }) => {
    const leagueId = params.leagueId;

    let leagueResponse = await fetch(`/leagues/${ leagueId }`)
    league.set(await leagueResponse.json());

    let playersResponse = await fetch(`/leagues/${ leagueId }/players`);
    players.set(await playersResponse.json());

    let playerSpotlightResponse = await fetch(`/leagues/${ leagueId }/players/spotlight`, { method: 'PUT' })
    playerSpotlight.set(await playerSpotlightResponse.json());

    return { };
}


