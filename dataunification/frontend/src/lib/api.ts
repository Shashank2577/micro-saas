const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8170';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

const api = {
    get: async (url: string) => {
        const res = await fetch(`${BASE_URL}${url}`, { headers });
        if (!res.ok) throw new Error(`Failed to GET ${url}: ${res.status}`);
        return { data: await res.json() };
    },
    post: async (url: string, body: any) => {
        const res = await fetch(`${BASE_URL}${url}`, {
            method: 'POST', headers, body: JSON.stringify(body)
        });
        if (!res.ok) throw new Error(`Failed to POST ${url}: ${res.status}`);
        return { data: await res.json() };
    }
};

export default api;
