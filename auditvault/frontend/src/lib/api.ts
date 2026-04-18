const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface Framework {
    id: string;
    tenantId: string;
    name: string;
    description: string;
    version: string;
    status: string;
}

export interface Evidence {
    id: string;
    tenantId: string;
    sourceApp: string;
    evidenceType: string;
    content: string;
    url: string;
    status: string;
    collectedAt: string;
}

export interface AuditPackage {
    id: string;
    tenantId: string;
    frameworkId: string;
    name: string;
    status: string;
    downloadUrl: string;
    generatedAt: string;
}

export const api = {
  frameworks: {
    list: async (): Promise<Framework[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/frameworks`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch frameworks: ${res.status}`);
      return res.json();
    }
  },
  evidence: {
    list: async (): Promise<Evidence[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/evidence`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch evidence: ${res.status}`);
      return res.json();
    },
    map: async (evidenceId: string, frameworkId: string) => {
        const res = await fetch(`${BASE_URL}/api/v1/evidence/${evidenceId}/map?frameworkId=${frameworkId}`, { method: 'POST', headers });
        if (!res.ok) throw new Error('Failed to map evidence');
        return res.json();
    }
  },
  packages: {
    list: async (): Promise<AuditPackage[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/packages`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch packages: ${res.status}`);
      return res.json();
    },
    generate: async (frameworkId: string, name: string) => {
        const res = await fetch(`${BASE_URL}/api/v1/packages`, {
            method: 'POST',
            headers,
            body: JSON.stringify({ frameworkId, name })
        });
        if (!res.ok) throw new Error('Failed to generate package');
        return res.json();
    }
  }
};
