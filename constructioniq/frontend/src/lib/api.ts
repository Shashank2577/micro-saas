const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8161';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface Project {
  id: string;
  name: string;
  status: string;
  budget: number;
  startDate: string;
  endDate: string;
}

export const api = {
  projects: {
    list: async (): Promise<Project[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/projects`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch projects: ${res.status}`);
      return res.json();
    },
    create: async (data: Omit<Project, 'id'>): Promise<Project> => {
      const res = await fetch(`${BASE_URL}/api/v1/projects`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create project: ${res.status}`);
      return res.json();
    },
  },
};
