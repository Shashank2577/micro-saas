const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8171/api';
const TENANT_ID = '00000000-0000-0000-0000-000000000001';

async function fetchWithTenant(endpoint: string, options: RequestInit = {}) {
  const headers = {
    'Content-Type': 'application/json',
    'X-Tenant-ID': TENANT_ID,
    ...options.headers,
  };
  
  const response = await fetch(`${BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });
  
  if (!response.ok) {
    throw new Error(`API error: ${response.status}`);
  }
  
  return response.json();
}

export const api = {
  getInsights: () => fetchWithTenant('/insights'),
  getInsight: (id: string) => fetchWithTenant(`/insights/${id}`),
  updateInsightStatus: (id: string, status: string) => fetchWithTenant(`/insights/${id}/status`, {
    method: 'PUT',
    body: JSON.stringify({ status })
  }),
  getAlertRules: () => fetchWithTenant('/alerts/rules'),
  createAlertRule: (rule: any) => fetchWithTenant('/alerts/rules', {
    method: 'POST',
    body: JSON.stringify(rule)
  }),
  updateAlertRule: (id: string, rule: any) => fetchWithTenant(`/alerts/rules/${id}`, {
    method: 'PUT',
    body: JSON.stringify(rule)
  }),
  getCustomRules: () => fetchWithTenant('/discovery/rules'),
  createCustomRule: (rule: any) => fetchWithTenant('/discovery/rules', {
    method: 'POST',
    body: JSON.stringify(rule)
  }),
  deleteCustomRule: (id: string) => fetchWithTenant(`/discovery/rules/${id}`, {
    method: 'DELETE'
  }),
};
