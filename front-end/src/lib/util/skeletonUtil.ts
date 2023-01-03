import type { ModalComponent } from '@skeletonlabs/skeleton';

export enum ModalType {
    COMPONENT = 'component',
    COFIRM = 'confirm',
}

export const buildModalComponent = (component: any, props: any = {}): ModalComponent => {
    return {
        ref: component,
        props,
    };
}
