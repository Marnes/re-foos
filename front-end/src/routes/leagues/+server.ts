import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { error, json } from '@sveltejs/kit';
import type { League } from '$src/models/league/league';

export const GET: RequestHandler = async ({ locals }) => {
    let response = await api.get(`/leagues`, locals.session?.jwt);

    return json(await response.json());
}

export const POST: RequestHandler = async ({ request,  locals }) => {
    const league: League = await request.json();

    const response = await api.post(`/leagues`, league, locals.session.jwt);

    if (response.status !== 200) {
        throw error(response.status)
    }

    return json(await response.json());
}
