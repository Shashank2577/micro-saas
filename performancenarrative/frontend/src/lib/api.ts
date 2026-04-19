const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8093';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

const api = {
  get: async (path: string) => {
    const res = await fetch(`${BASE_URL}/api/v1${path}`, { headers });
    if (!res.ok) throw new Error(`Failed to fetch ${path}: ${res.status}`);
    return res.json();
  },
  post: async (path: string, body: any) => {
    const res = await fetch(`${BASE_URL}/api/v1${path}`, {
      method: 'POST',
      headers,
      body: JSON.stringify(body)
    });
    if (!res.ok) throw new Error(`Failed to post ${path}: ${res.status}`);
    return res.json();
  },
  patch: async (path: string, body: any) => {
    const res = await fetch(`${BASE_URL}/api/v1${path}`, {
      method: 'PATCH',
      headers,
      body: JSON.stringify(body)
    });
    if (!res.ok) throw new Error(`Failed to patch ${path}: ${res.status}`);
    return res.json();
  }
};

export default api;
