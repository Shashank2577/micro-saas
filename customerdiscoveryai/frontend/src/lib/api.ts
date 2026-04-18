const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export const api = {
  get: async (url: string) => {
    const res = await fetch(`${BASE_URL}${url}`, { headers });
    if (!res.ok) throw new Error(`GET request failed: ${res.status}`);
    const data = await res.json();
    return { data };
  },
  post: async (url: string, body?: any) => {
    const res = await fetch(`${BASE_URL}${url}`, {
      method: 'POST',
      headers,
      body: body ? JSON.stringify(body) : undefined,
    });
    if (!res.ok) throw new Error(`POST request failed: ${res.status}`);
    const data = await res.json();
    return { data };
  }
};
