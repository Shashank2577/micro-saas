const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8143';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export const api = {
  calls: {
    list: async () => {
      const res = await fetch(`${BASE_URL}/api/calls`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch calls: ${res.status}`);
      return res.json();
    },
    get: async (id: string) => {
      const res = await fetch(`${BASE_URL}/api/calls/${id}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch call: ${res.status}`);
      return res.json();
    },
  },
  scorecards: {
    get: async (repId: string) => {
      const res = await fetch(`${BASE_URL}/api/scorecards/reps/${repId}`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch scorecard: ${res.status}`);
      return res.json();
    }
  }
};
