import { writable, type Writable } from 'svelte/store';
import type { PlayerSpotlight } from '$src/models/player/player';

export const spotlightStore: Writable<PlayerSpotlight> = writable();
