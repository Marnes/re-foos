import { writable, type Writable } from 'svelte/store';
import type { Session } from '$src/models/session/session';

export const session: Writable<Session | null> = writable();
