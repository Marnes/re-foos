import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { error, json } from '@sveltejs/kit';
import type { GameCaptureRequest, MatchCaptureRequest } from '$src/models/match/captureRequest';

export const POST: RequestHandler = async ({ request, params, locals }) => {
    const match: MatchCaptureRequest = await request.json();

    const response = await api.post(`/leagues/${ params.leagueId }/matches`, match, locals.session.jwt);

    if (response.status !== 200) {
        throw error(response.status)
    }

    return json(await response.json());
}
