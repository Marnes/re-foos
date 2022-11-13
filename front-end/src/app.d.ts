// See https://kit.svelte.dev/docs/types#app
// for information about these interfaces
// and what to do when importing types
import type { User } from "src/models/user";

declare namespace App {
	interface Locals {
        jwt: string,
        user: { username: string; avatar: string };
    }
	// interface PageData {}
	// interface Error {}
	// interface Platform {}
}

