/** @type {import('./$types').PageLoad} */
import { sessionStore } from "$src/stores/session-store";
import { isExpired } from "$src/lib/util/jwt-util";
import type { PageLoad } from './$types';

export const load: PageLoad = ({ data }) => {
    if (data?.user && !isExpired(data.user.exp)) {
        sessionStore.set(data.user);
    } else {
        sessionStore.set(null);
    }

    return data;
}
