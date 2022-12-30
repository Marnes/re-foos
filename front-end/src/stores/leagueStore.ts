import { writable, type Writable } from 'svelte/store';
import type { League } from '$src/models/league/league';
import type { Player, PlayerMin } from '$src/models/player/player';
import type { Highlight } from '$src/models/highlight/highlight';

export const league: Writable<League | null> = writable(null);
export const players: Writable<Player[]> = writable([]);
export const playerSpotlight: Writable<Player | PlayerMin | null> = writable(null);
export const highlights: Writable<Highlight[]> = writable([]);
