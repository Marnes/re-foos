import type { PageLoad } from './$types';
import { highlightStore } from '$src/stores/highlightStore';

export const load: PageLoad = async ({ fetch }) => {
    let highlightsResponse = await fetch('/leagues/1/highlights');
    const highlights = await highlightsResponse.json();
    highlightStore.set(highlights)
    return {};
}


