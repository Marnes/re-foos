import { writable, type Writable } from 'svelte/store';
import type { Player, PlayerMin} from '$src/models/player/player';

export const spotlightStore: Writable<Player | PlayerMin | null> = writable(null);
