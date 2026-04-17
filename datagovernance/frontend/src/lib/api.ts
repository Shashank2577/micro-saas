const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8112';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface DataRetentionPolicy {
  id: string;
  dataType: string;
  retentionDays: number;
  createdAt: string;
  updatedAt: string;
}

export interface DataSubjectRequest {
  id: string;
  subjectEmail: string;
  requestType: string;
  status: string;
  createdAt: string;
  completedAt: string | null;
}

export interface ConsentRecord {
  id: string;
  userEmail: string;
  processingPurpose: string;
  isGranted: boolean;
  timestamp: string;
}

export interface AuditLog {
  id: string;
  actor: string;
  action: string;
  resource: string;
  timestamp: string;
  details: string;
}

export interface DataLineageNode {
  id: string;
  fieldName: string;
  originService: string;
  currentService: string;
  transformationLogic: string;
  createdAt: string;
}

export const api = {
  policies: {
    list: async (): Promise<DataRetentionPolicy[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/policies`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch policies: ${res.status}`);
      return res.json();
    },
    create: async (data: Omit<DataRetentionPolicy, 'id' | 'createdAt' | 'updatedAt'>): Promise<DataRetentionPolicy> => {
      const res = await fetch(`${BASE_URL}/api/v1/policies`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create policy: ${res.status}`);
      return res.json();
    },
    delete: async (id: string): Promise<void> => {
      const res = await fetch(`${BASE_URL}/api/v1/policies/${id}`, { method: 'DELETE', headers });
      if (!res.ok) throw new Error(`Failed to delete policy: ${res.status}`);
    }
  },
  dsar: {
    list: async (): Promise<DataSubjectRequest[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/dsar`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch DSARs: ${res.status}`);
      return res.json();
    },
    create: async (data: Omit<DataSubjectRequest, 'id' | 'status' | 'createdAt' | 'completedAt'>): Promise<DataSubjectRequest> => {
      const res = await fetch(`${BASE_URL}/api/v1/dsar`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create DSAR: ${res.status}`);
      return res.json();
    },
    process: async (id: string): Promise<DataSubjectRequest> => {
      const res = await fetch(`${BASE_URL}/api/v1/dsar/${id}/process`, { method: 'POST', headers });
      if (!res.ok) throw new Error(`Failed to process DSAR: ${res.status}`);
      return res.json();
    }
  },
  consent: {
    list: async (email: string): Promise<ConsentRecord[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/consent/${email}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch consent records: ${res.status}`);
      return res.json();
    },
    create: async (data: Omit<ConsentRecord, 'id' | 'timestamp'>): Promise<ConsentRecord> => {
      const res = await fetch(`${BASE_URL}/api/v1/consent`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create consent record: ${res.status}`);
      return res.json();
    }
  },
  pii: {
    detect: async (text: string): Promise<{ piiTypes: string[] }> => {
      const res = await fetch(`${BASE_URL}/api/v1/pii/detect`, {
        method: 'POST', headers, body: JSON.stringify({ text }),
      });
      if (!res.ok) throw new Error(`Failed to detect PII: ${res.status}`);
      return res.json();
    }
  },
  lineage: {
    get: async (field: string): Promise<DataLineageNode[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/lineage?field=${field}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch lineage: ${res.status}`);
      return res.json();
    }
  },
  audit: {
    list: async (): Promise<AuditLog[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/audit`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch audit logs: ${res.status}`);
      return res.json();
    }
  }
};
