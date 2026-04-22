const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8093';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export const api = {
  policies: {
    list: async (): Promise<any[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/policies`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch policies: ${res.status}`);
      return res.json();
    },
    get: async (id: string): Promise<any> => {
      const res = await fetch(`${BASE_URL}/api/v1/policies/${id}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch policy: ${res.status}`);
      return res.json();
    },
  },
  gaps: {
    list: async (): Promise<any[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/gaps`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch gaps: ${res.status}`);
      return res.json();
    },
  },
  categories: {
    list: async (): Promise<any[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/categories`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch categories: ${res.status}`);
      return res.json();
    },
  },
};
