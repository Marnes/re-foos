import { writable, type Writable } from 'svelte/store';
import type { League } from '$src/models/league/league';

export const leagueStore: Writable<League | null> = writable(null);
