import { type Writable, writable } from "svelte/store";

export const captureDrawerStore: Writable<boolean> = writable(false);
