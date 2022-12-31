import type { Page } from '@sveltejs/kit';
import _ from 'lodash';

export const createRail = (title: string, link: string, icon: string, page: Page): any => {
    return { title, link, icon, selected: _.startsWith(page.url.pathname, link) };
}
