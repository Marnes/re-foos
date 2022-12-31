import { redirect } from '@sveltejs/kit';

export const load = async () => {
    throw redirect(307, '/leagues/1/leaderboard');
}

