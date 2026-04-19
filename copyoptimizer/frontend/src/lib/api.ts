import { BaseEntity } from '../types';

export const fetchAPI = async <T,>(endpoint: string): Promise<T> => {
  const res = await fetch(`/api/v1/copy${endpoint}`, {
    headers: {
      'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
    },
  });
  if (!res.ok) {
    throw new Error('API error');
  }
  return res.json();
};
