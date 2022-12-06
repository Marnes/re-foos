import { writable, type Writable } from 'svelte/store';
import type { Highlight } from '$src/models/highlight/highlight';

export const highlightStore: Writable<Highlight[]> = writable([]);
