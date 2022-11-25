import type { Writable } from "svelte/store";
import { writable } from "svelte/store";
import type { Dialog } from "$src/models/dialog";

export const dialogStore: Writable<Dialog | null> = writable(null);
