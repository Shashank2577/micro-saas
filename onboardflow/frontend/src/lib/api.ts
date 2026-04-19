const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8125';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface OnboardingPlan {
  id: string;
  name: string;
  status: string;
}

export interface AnalyticsData {
  averageTimeToProductivity: number;
}

export interface EcosystemApp {
  id: string;
  name: string;
  displayName: string;
  description?: string;
  status: 'active' | 'beta' | 'planned';
  baseUrl?: string;
  lastHeartbeatAt?: string;
}

export const api = {
  plans: {
    list: async (): Promise<OnboardingPlan[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/onboarding/onboarding-plans`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch plans: ${res.status}`);
      return res.json();
    },
  },
  analytics: {
    getTimeToProductivity: async (): Promise<AnalyticsData> => {
      // Mocked
      return { averageTimeToProductivity: 0 };
    },
  },
};
