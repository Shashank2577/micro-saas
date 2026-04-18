const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8144/api';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

async function handleResponse(res: Response) {
  if (!res.ok) {
    throw new Error(`API Error: ${res.status} ${res.statusText}`);
  }
  return { data: await res.json() };
}

export const api = {
  get: async (path: string) => {
    const res = await fetch(`${BASE_URL}${path}`, { headers });
    return handleResponse(res);
  },
  post: async (path: string, body?: any) => {
    const res = await fetch(`${BASE_URL}${path}`, {
      method: 'POST',
      headers,
      body: body ? JSON.stringify(body) : undefined,
    });
    // Check if empty response
    if (res.status === 204 || res.headers.get('content-length') === '0') {
      return { data: null };
    }
    return handleResponse(res);
  },
};
