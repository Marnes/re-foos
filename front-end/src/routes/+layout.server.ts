import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ locals, fetch }) => {
    return { jwt: locals.jwt, user: locals.user }
}

