import api from '$src/lib/api';
import type { RequestHandler } from '@sveltejs/kit';
import { error, json } from '@sveltejs/kit';
import type { League } from '$src/models/league/league';

export const GET: RequestHandler = async ({ params , locals}) => {
    let response = await api.get(`/leagues/${params.leagueId}`, locals.session?.jwt);

    return json(await response.json());
}

