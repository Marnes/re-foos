import type { PageLoad } from './$types';
import { highlightStore } from '$src/stores/highlightStore';

export const load: PageLoad = async ({ fetch, params }) => {
    let highlightsResponse = await fetch(`/leagues/${params.leagueId}/highlights`);
    const highlights = await highlightsResponse.json();
    highlightStore.set(highlights)
    return {};
}


