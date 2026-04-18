const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8149/api/v1';

export const fetchApi = async (endpoint: string, options: RequestInit = {}) => {
  const res = await fetch(`${BASE_URL}${endpoint}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
      ...options.headers,
    },
  });
  if (!res.ok) {
    throw new Error(`API error: ${res.statusText}`);
  }
  return res.json();
};
