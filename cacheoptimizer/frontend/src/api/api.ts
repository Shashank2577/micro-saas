const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8114';

export interface CachePolicy {
  id: string;
  appName: string;
  namespace: string;
  ttlSeconds: number;
  strategy: 'CACHE_ASIDE' | 'WRITE_THROUGH' | 'WRITE_BEHIND';
  compressionEnabled: boolean;
  staleWhileRevalidate: boolean;
  staleTtlSeconds?: number;
}

export interface CacheAnalytics {
  id: string;
  namespace: string;
  hitCount: number;
  missCount: number;
  totalSizeBytes: number;
  timestamp: string;
}

export const api = {
  getPolicies: async (): Promise<CachePolicy[]> => {
    const res = await fetch(`${BASE_URL}/api/v1/policies`, {
      headers: {
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
      }
    });
    if (!res.ok) throw new Error('Failed to fetch policies');
    return res.json();
  },
  createPolicy: async (policy: Partial<CachePolicy>): Promise<CachePolicy> => {
    const res = await fetch(`${BASE_URL}/api/v1/policies`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
      },
      body: JSON.stringify(policy),
    });
    if (!res.ok) throw new Error('Failed to create policy');
    return res.json();
  },
  getAnalytics: async (): Promise<CacheAnalytics[]> => {
    const res = await fetch(`${BASE_URL}/api/v1/analytics`, {
      headers: {
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
      }
    });
    if (!res.ok) throw new Error('Failed to fetch analytics');
    return res.json();
  }
};
