const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface Connector {
  id: string;
  name: string;
  type: string;
  config: Record<string, unknown>;
  status: 'ACTIVE' | 'ERROR' | 'DISCONNECTED';
  createdAt: string;
}

export interface Integration {
  id: string;
  name: string;
  sourceConnectorId: string;
  targetConnectorId: string;
  status: 'ACTIVE' | 'PAUSED' | 'ERROR';
  createdAt: string;
}

export interface FieldMapping {
  id: string;
  integrationId: string;
  sourceField: string;
  targetField: string;
  transformLogic?: string;
  isAiSuggested: boolean;
  confidenceScore?: number;
}

export interface SyncHistory {
  id: string;
  integrationId: string;
  status: 'SUCCESS' | 'PARTIAL' | 'FAILED';
  recordsProcessed: number;
  recordsFailed: number;
  errorMessage?: string;
  startedAt: string;
  completedAt?: string;
}

export const api = {
  connectors: {
    list: async (): Promise<Connector[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/connectors`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch connectors`);
      return res.json();
    },
    create: async (data: Omit<Connector, 'id' | 'status' | 'createdAt'>): Promise<Connector> => {
      const res = await fetch(`${BASE_URL}/api/v1/connectors`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create connector`);
      return res.json();
    },
  },
  integrations: {
    list: async (): Promise<Integration[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/integrations`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch integrations`);
      return res.json();
    },
    create: async (data: Omit<Integration, 'id' | 'status' | 'createdAt'>): Promise<Integration> => {
      const res = await fetch(`${BASE_URL}/api/v1/integrations`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create integration`);
      return res.json();
    },
  },
  mappings: {
    list: async (integrationId: string): Promise<FieldMapping[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/integrations/${integrationId}/mappings`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch mappings`);
      return res.json();
    },
    suggest: async (integrationId: string): Promise<FieldMapping[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/integrations/${integrationId}/mappings/suggest`, {
        method: 'POST', headers,
      });
      if (!res.ok) throw new Error(`Failed to suggest mappings`);
      return res.json();
    },
  },
  history: {
    list: async (integrationId: string): Promise<SyncHistory[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/integrations/${integrationId}/history`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch sync history`);
      return res.json();
    },
  }
};
