import type { ToastSettings } from '@skeletonlabs/skeleton';

export class ToastMessage {
    static failure(message: string, classes: string = '', timeout: number = 3000, autohide: boolean = true): ToastSettings {
        return { message, timeout, autohide, preset: 'warning', classes: `${ classes }` }
    }

    static success(message: string, classes: string = '', timeout: number = 3000, autohide: boolean = true): ToastSettings {
        return { message, timeout, autohide, preset: 'secondary', classes: `${ classes }'` }
    }

    static neutral(message: string, classes: string = '', timeout: number = 3000, autohide: boolean = true): ToastSettings {
        return { message, timeout, autohide, preset: 'warning', classes: `${ classes }` }
    }
}
