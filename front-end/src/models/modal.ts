import type { ModalComponent, ModalSettings } from '@skeletonlabs/skeleton';
import _ from 'lodash';

/**
 * @deprecated The method should not be used. Use skeletonUtil#enum with normal method
 */
export class Modal {

    static component(title: string, customComponent: any, props?: any, response?: any): ModalSettings {
        const modalComponent: ModalComponent = {
            ref: customComponent,
            props,
        };

        return {
            type: 'component',
            component: modalComponent,
            title,
            response: !_.isNil(response) ? response : () => ({})
        };
    }

    static confirm(title: string, body: string, confirm?: any, cancel?: any ): ModalSettings {
        return  {
            type: 'confirm',
            title,
            body,
            response: (r: boolean) => {
                if (r) {
                    if (confirm) confirm();
                } else {
                    if (cancel) cancel();
                }
            }
        };
    }
}
