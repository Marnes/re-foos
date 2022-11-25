import { PUBLIC_HOST } from '$env/static/public'
import _ from 'lodash';

const BASE = import.meta.env.VITE_HOST;

type Request = {
    method: string;
    path: string;
    data?: unknown;
    token?: string;
}

type RequestOptions = {
    method: string;
    headers: { 'content-type': string, 'Authorization'?: string };
    body?: string | FormData;
}

const send = async ({ method, path, data, token }: Request): Promise<Response> => {
    const opts: RequestOptions = { method, headers: { 'content-type': 'application/json' } };

    if (_.isString(data)) {
        opts.body = data;
    } else if (data instanceof FormData) {
        opts.headers['content-type'] = 'x-www-form-urlencoded';
        opts.body = data;
    } else {
        opts.body = JSON.stringify(data);
    }

    if (token) {
        opts.headers['Authorization'] = `Bearer ${ token }`;
    }

    return await fetch(`${ PUBLIC_HOST }/api${ path }`, opts);
}

export const getAssetPath = (path: string): string => {
    return `${ PUBLIC_HOST }/assets${ path }`;
}

export default {
    get(path: string, token?: string): Promise<Response> {
        return send({ method: 'GET', path, token });
    },
    del(path: string, token?: string): Promise<Response> {
        return send({ method: 'DELETE', path, token });
    },
    post(path: string, data: unknown, token?: string): Promise<Response> {
        return send({ method: 'POST', path, data, token });
    },
    put(path: string, data: unknown, token?: string): Promise<Response> {
        return send({ method: 'PUT', path, data, token });
    },
};
