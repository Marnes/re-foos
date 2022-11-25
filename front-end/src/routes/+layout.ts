import { sessionStore } from '$src/stores/sessionStore';
import { Session } from '$src/models/session/session';
import type { LayoutLoad } from './$types';

const setSession = async (data: any) => {
    if (data.session) {
        sessionStore.set(new Session(data.session.user, data.session.jwt))
    } else {
        sessionStore.set(null);
    }
}

export const load: LayoutLoad = async ({ data }) => {
    await setSession(data)
    return {};
}
