import type { LayoutLoad } from './$types';
import { spotlightStore } from '$src/stores/spotlightStore';
import { leagueStore } from '$src/stores/leagueStore';

export const load: LayoutLoad = async ({ fetch, params }) => {
    const leagueId = params.leagueId;

    let leagueResponse = await fetch(`/leagues/${ leagueId }`)
    leagueStore.set(await leagueResponse.json());

    let playersResponse = await fetch(`/leagues/${ leagueId }/players`);
    const players = await playersResponse.json();

    let playerSpotlightResponse = await fetch(`/leagues/${ leagueId }/players/spotlight`, { method: 'PUT' })
    spotlightStore.set(await playerSpotlightResponse.json());

    return { players };
}


