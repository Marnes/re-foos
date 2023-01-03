import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const POST: RequestHandler = async ({ url, params, locals }) => {
    await api.post(`/leagues/${ params.leagueId }/season`, {}, locals.session.jwt);
    return json({});
}
