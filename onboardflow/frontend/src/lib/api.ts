const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8125';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface OnboardingPlan {
  id: string;
  employeeId: string;
  role: string;
  department: string;
  startDate: string;
  plan30Day: Record<string, unknown>;
  plan60Day: Record<string, unknown>;
  plan90Day: Record<string, unknown>;
  status: 'DRAFT' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
  createdAt: string;
}

export interface AnalyticsData {
  averageTimeToProductivity: number;
}

export const api = {
  plans: {
    list: async (): Promise<OnboardingPlan[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/plans`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch plans: ${res.status}`);
      return res.json();
    },
  },
  analytics: {
    getTimeToProductivity: async (): Promise<AnalyticsData> => {
      const res = await fetch(`${BASE_URL}/api/v1/analytics/time-to-productivity`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch analytics: ${res.status}`);
      return res.json();
    },
  },
};
