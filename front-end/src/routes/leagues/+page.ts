import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch }) => {
    let leaguesResponse = await fetch('/leagues');
    const leagues = await leaguesResponse.json();
    return { leagues };
}


