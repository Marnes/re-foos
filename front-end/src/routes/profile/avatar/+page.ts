import type { PageLoad } from './$types';


export const load: PageLoad = async ({ data, fetch }) => {
    const avatarsResponse = await fetch('/profile/avatar');
    const avatars = await avatarsResponse.json();

    return { avatars }
}
