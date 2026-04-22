import axios from 'axios';

export const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8127/api/v1/people-analytics',
  headers: {
    'Content-Type': 'application/json',
    'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
  },
});

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

export default api;
