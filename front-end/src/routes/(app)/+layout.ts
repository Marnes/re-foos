import type { LayoutLoad } from './$types';
import { spotlightStore } from '$src/stores/spotlightStore';

export const load: LayoutLoad = async ({ fetch }) => {
    let playersResponse = await fetch('/leagues/1/players');
    const players = await playersResponse.json();

    let playerSpotlightResponse = await fetch('/leagues/1/players/spotlight', { method: 'PUT' })
    spotlightStore.set(await playerSpotlightResponse.json());

    return { players };
}


