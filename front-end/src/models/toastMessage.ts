import type { ToastSettings } from '@skeletonlabs/skeleton';

export class ToastMessage {
    static failure(message: string, classes: string = '', timeout: number = 3000, autohide: boolean = true): ToastSettings {
        return { message, timeout, autohide, classes: `${ classes } bg-red-500` }
    }

    static success(message: string, classes: string = '', timeout: number = 3000, autohide: boolean = true): ToastMessage {
        return { message, timeout, autohide, classes: `${ classes } bg-green-500` }
    }

    static neutral(message: string, classes: string = '', timeout: number = 3000, autohide: boolean = true): ToastMessage {
        return { message, timeout, autohide, classes: `${ classes }` }
    }
}
