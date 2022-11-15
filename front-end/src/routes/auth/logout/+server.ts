import type { RequestHandler } from '@sveltejs/kit';
import { json } from "@sveltejs/kit";

export const DELETE: RequestHandler = async ({ cookies }) => {
    await cookies.delete("jwt", { path: '/' });
    return json({})
}

