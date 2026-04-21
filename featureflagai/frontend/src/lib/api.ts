const API_BASE = '/api';

export const api = {
  get: async (endpoint: string) => {
    const res = await fetch(`${API_BASE}${endpoint}`, {
      headers: {
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' // mock tenant for development
      }
    });
    if (!res.ok) throw new Error('API Error');
    return { data: await res.json() };
  },
  post: async (endpoint: string, data: any) => {
    const res = await fetch(`${API_BASE}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
      },
      body: JSON.stringify(data)
    });
    if (!res.ok) throw new Error('API Error');
    // If response is empty (e.g. 200 OK without body)
    const text = await res.text();
    return { data: text ? JSON.parse(text) : {} };
  }
};

export default api;
