import type { PageServerLoad } from './$types';
import { redirect } from '@sveltejs/kit';

export const load: PageServerLoad = async ({ locals }) => {
    if (!locals.session?.user?.admin) {
        throw redirect(307, '/');
    }

    return {};
}

