import type { Writable } from 'svelte/store';
import type { User } from "$src/models/user";
import { writable } from "svelte/store";

export const sessionStore: Writable<User | null> = writable();
