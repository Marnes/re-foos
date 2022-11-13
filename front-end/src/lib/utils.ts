export const get = (endpoint: string): Promise<Response> => {
    return fetch(endpoint, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export const postForm = (endpoint: string, formData?: FormData): Promise<Response> => {
    return fetch(endpoint, {
        method: 'POST',
        body: formData || new FormData(),
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

export const del = (endpoint: string, data: unknown): Promise<Response> => {
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

