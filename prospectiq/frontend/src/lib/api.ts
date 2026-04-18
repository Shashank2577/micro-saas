export const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8141';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface Prospect {
  id: string;
  name: string;
  domain: string;
  industry: string;
  region: string;
  crmId?: string;
}

export interface Signal {
  id: string;
  type: string;
  source: string;
  content: string;
  detectedAt: string;
}

export interface ProspectBrief {
  id: string;
  content: string;
  status: string;
}

export interface ICPProfile {
  id: string;
  name: string;
  criteria: Record<string, unknown>;
}

export const api = {
  prospects: {
    list: async (): Promise<Prospect[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/prospects`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch prospects`);
      return res.json();
    },
    get: async (id: string): Promise<Prospect> => {
      const res = await fetch(`${BASE_URL}/api/v1/prospects/${id}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch prospect`);
      return res.json();
    },
  },
  signals: {
    forProspect: async (prospectId: string): Promise<Signal[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/prospects/${prospectId}/signals`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch signals`);
      return res.json();
    },
  },
  briefs: {
    getLatest: async (prospectId: string): Promise<ProspectBrief> => {
      const res = await fetch(`${BASE_URL}/api/v1/prospects/${prospectId}/briefs`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch brief`);
      return res.json();
    },
    generate: async (prospectId: string): Promise<ProspectBrief> => {
      const res = await fetch(`${BASE_URL}/api/v1/prospects/${prospectId}/briefs`, { method: 'POST', headers });
      if (!res.ok) throw new Error(`Failed to generate brief`);
      return res.json();
    },
  },
  icp: {
    list: async (): Promise<ICPProfile[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/icp`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch icp profiles`);
      return res.json();
    },
  }
};
