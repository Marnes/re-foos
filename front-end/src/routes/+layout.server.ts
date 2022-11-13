import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ locals, fetch }) => {
    let playersResponse = await fetch(`/players`);

    return { jwt: locals.jwt, user: locals.user, players: await playersResponse.json() }
}

