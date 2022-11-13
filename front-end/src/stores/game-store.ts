import { type Writable, writable } from "svelte/store";

export const drawerStore: Writable<boolean> = writable(false);
