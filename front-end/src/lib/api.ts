const base = 'http://localhost:8080/api';

type Request = {
    method: string;
    path: string;
    data?: unknown;
    token?: string;
}

type RequestOptions = {
    method: string;
    headers: { 'content-type': string, 'Authorization'?: string };
    body?: string;
}

const send = async ({ method, path, data, token }: Request): Promise<Response> => {
    const opts: RequestOptions = { method, headers: { 'content-type': 'application/json' } };

    if (data) {
        opts.body = JSON.stringify(data);
    }

    if (token) {
        opts.headers['Authorization'] = `Bearer ${ token }`;
    }

    return await fetch(`${ base }/${ path }`, opts);
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
    }
};
