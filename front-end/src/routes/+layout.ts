/** @type {import('./$types').PageLoad} */
import { sessionStore } from "$src/stores/session-store";
import { isExpired } from "$src/lib/util/jwt-util";
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ data, fetch }) => {
    let playersResponse = await fetch('players');

    if (data?.user && !isExpired(data.user.exp)) {
        sessionStore.set(data.user);
    } else {
        sessionStore.set(null);
    }

    const players = await playersResponse.json();

    return { ...data, players };
}

