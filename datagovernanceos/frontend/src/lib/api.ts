const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8283';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface DataAsset {
  id?: string;
  name: string;
  type: string;
  description: string;
  classification?: string;
  piiStatus?: boolean;
  owner: string;
}

export interface GovernancePolicy {
  id?: string;
  name: string;
  description: string;
  ruleDefinition: string;
  enforcementLevel: string;
  status: string;
}

export interface ComplianceAudit {
  id: string;
  assetId: string;
  policyId: string;
  status: string;
  findings: string;
  auditedAt: string;
}

export const api = {
  assets: {
    list: async (): Promise<DataAsset[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/assets`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch assets: ${res.status}`);
      return res.json();
    },
    create: async (data: DataAsset): Promise<DataAsset> => {
      const res = await fetch(`${BASE_URL}/api/v1/assets`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create asset: ${res.status}`);
      return res.json();
    },
  },
  policies: {
    list: async (): Promise<GovernancePolicy[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/policies`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch policies: ${res.status}`);
      return res.json();
    },
  },
  audits: {
    list: async (): Promise<ComplianceAudit[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/audits`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch audits: ${res.status}`);
      return res.json();
    },
    run: async (assetId: string): Promise<void> => {
      const res = await fetch(`${BASE_URL}/api/v1/audits/run?assetId=${assetId}`, {
        method: 'POST', headers,
      });
      if (!res.ok) throw new Error(`Failed to run audits: ${res.status}`);
    }
  }
};
export default api;
