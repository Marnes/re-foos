import { PUBLIC_HOST } from '$env/dynamic/public';

export const get = (endpoint: string): Promise<Response> => {
    return fetch(endpoint, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export const post = (endpoint: string, data?: unknown): Promise<Response> => {
    return fetch(endpoint, {
        method: 'POST',
        credentials: 'include',
        body: JSON.stringify(data || {}),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export const put = (endpoint: string, data?: unknown): Promise<Response> => {
    return fetch(endpoint, {
        method: 'put',
        credentials: 'include',
        body: JSON.stringify(data || {}),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export const del = (endpoint: string, data?: unknown): Promise<Response> => {
    return fetch(endpoint, {
        method: 'delete',
        credentials: 'include',
        body: JSON.stringify(data || {}),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export const url = (path: string, params: any): string => {
    const query = new URLSearchParams();

    Object.entries(params).forEach(([key, val]) => query.set(key, <string>val));

    return `${ path }?${ query.toString() }`;
}

export const getAssetPath = (path: string): string => {
    return `${ PUBLIC_HOST }/assets${ path }`;
}

export const isSuccess = (statusCode: Number): boolean => {
    return 200 <= statusCode && statusCode < 300;
}
