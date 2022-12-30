import type { PageLoad } from './$types';
import { highlights } from '$src/stores/leagueStore';

export const load: PageLoad = async ({ fetch, params }) => {
    let highlightsResponse = await fetch(`/leagues/${params.leagueId}/highlights`);
    highlights.set(await highlightsResponse.json());
    return {};
}


