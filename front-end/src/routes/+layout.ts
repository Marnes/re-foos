import { session } from '$src/stores/sessionStore';
import { Session } from '$src/models/session/session';
import type { LayoutLoad } from './$types';

const setSession = async (data: any) => {
    if (data.session) {
        session.set(new Session(data.session.user, data.session.jwt))
    } else {
        session.set(null);
    }
}

export const load: LayoutLoad = async ({ data }) => {
    await setSession(data)
    return {};
}
