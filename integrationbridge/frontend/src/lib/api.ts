const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8109';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface Integration {
  id: string;
  provider: string;
  status: 'HEALTHY' | 'ERROR' | 'PENDING';
  authType: string;
  createdAt: string;
  updatedAt: string;
}

export interface SyncJob {
  id: string;
  integrationId: string;
  scheduleCron: string;
  sourceEntity: string;
  targetEntity: string;
  status: string;
  lastRunAt: string | null;
  nextRunAt: string | null;
}

export interface AuditLog {
  id: string;
  action: string;
  status: string;
  recordsProcessed: number;
  errorMessage: string | null;
  createdAt: string;
}

export const api = {
  integrations: {
    list: async (): Promise<Integration[]> => {
      const res = await fetch(`${BASE_URL}/api/integrations`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch integrations: ${res.status}`);
      return res.json();
    },
    get: async (id: string): Promise<Integration> => {
      const res = await fetch(`${BASE_URL}/api/integrations/${id}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch integration: ${res.status}`);
      return res.json();
    },
    create: async (provider: string, authType: string): Promise<Integration> => {
      const res = await fetch(`${BASE_URL}/api/integrations`, {
        method: 'POST',
        headers,
        body: JSON.stringify({ provider, authType })
      });
      if (!res.ok) throw new Error(`Failed to create integration: ${res.status}`);
      return res.json();
    },
    syncJobs: async (id: string): Promise<SyncJob[]> => {
      const res = await fetch(`${BASE_URL}/api/integrations/${id}/sync-jobs`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch sync jobs: ${res.status}`);
      return res.json();
    },
    auditLogs: async (id: string): Promise<AuditLog[]> => {
      const res = await fetch(`${BASE_URL}/api/integrations/${id}/audit-logs`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch audit logs: ${res.status}`);
      return res.json();
    }
  }
};
