import { writable, type Writable } from 'svelte/store';

export const railStore: Writable<number> = writable(1);

