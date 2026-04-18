const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export const api = {
  get: async (endpoint: string, tenantId: string = 'tenant-123') => {
    const res = await fetch(`${BASE_URL}${endpoint}`, {
      headers: {
        'X-Tenant-ID': tenantId,
      },
    });
    if (!res.ok) throw new Error(`API error: ${res.statusText}`);
    return res.json();
  },
  post: async (endpoint: string, body: any, tenantId: string = 'tenant-123') => {
    const res = await fetch(`${BASE_URL}${endpoint}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': tenantId,
      },
      body: JSON.stringify(body),
    });
    if (!res.ok) throw new Error(`API error: ${res.statusText}`);
    return res.json();
  },
  put: async (endpoint: string, body: any, tenantId: string = 'tenant-123') => {
    const res = await fetch(`${BASE_URL}${endpoint}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': tenantId,
      },
      body: JSON.stringify(body),
    });
    if (!res.ok) throw new Error(`API error: ${res.statusText}`);
    return res.json();
  }
};
