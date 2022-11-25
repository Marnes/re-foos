import { Session } from '$src/models/session/session';

/// <reference types="@sveltejs/kit" />
declare global {
    namespace App {
        interface Locals {
            session: Session
        }

        // interface PageData {}
        // interface Error {}
        // interface Platform {}
    }
}

