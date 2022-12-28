import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const POST: RequestHandler = async ({ params, locals, url }) => {
    let response = await api.post(`/leagues/${ params.leagueId }/join?code=${url.searchParams.get('code')}`, {}, locals.session.jwt);

    return json(await response.json());
}
