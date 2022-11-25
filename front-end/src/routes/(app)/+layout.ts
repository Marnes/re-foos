import { spotlightStore } from '$src/stores/spotlightStore';
import type { LayoutLoad } from './$types';

export const load: LayoutLoad = async ({ fetch }) => {
    let playersResponse = await fetch('/leagues/1/players');
    let playerSpotlightResponse = await fetch('/leagues/1/players/spotlight')

    const players = await playersResponse.json();
    const playerSpotlight = await playerSpotlightResponse.json();

    spotlightStore.set(playerSpotlight)

    return { players };
}


