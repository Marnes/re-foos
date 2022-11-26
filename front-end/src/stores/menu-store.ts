import { writable, type Writable } from 'svelte/store';

export const menuDrawerStore: Writable<boolean> = writable(false);
