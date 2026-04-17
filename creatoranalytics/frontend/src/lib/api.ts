const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface EcosystemApp {
  id: string;
  name: string;
  displayName: string;
  baseUrl: string;
  status: 'ACTIVE' | 'INACTIVE' | 'ERROR';
  lastHeartbeatAt: string | null;
  registeredAt: string;
  manifest: Record<string, unknown>;
}

export interface EcosystemEvent {
  id: string;
  sourceApp: string;
  eventType: string;
  payload: Record<string, unknown>;
  createdAt: string;
}

export interface Workflow {
  id: string;
  name: string;
  enabled: boolean;
  lastRunAt: string | null;
  triggerCondition: Record<string, unknown>;
  steps: Record<string, unknown>[];
}

export const api = {
  apps: {
    list: async (): Promise<EcosystemApp[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/apps`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch apps: ${res.status}`);
      return res.json();
    },
    register: async (data: Omit<EcosystemApp, 'id' | 'status' | 'lastHeartbeatAt' | 'registeredAt'>): Promise<EcosystemApp> => {
      const res = await fetch(`${BASE_URL}/api/v1/apps/register`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to register app: ${res.status}`);
      return res.json();
    },
  },
  events: {
    list: async (limit = 50): Promise<EcosystemEvent[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/events?limit=${limit}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch events: ${res.status}`);
      return res.json();
    },
  },
  workflows: {
    list: async (): Promise<Workflow[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/workflows`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch workflows: ${res.status}`);
      return res.json();
    },
  },
};
