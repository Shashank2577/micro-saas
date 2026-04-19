const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090/api/v1/narratives';

export interface SupportingMetric {
  id: string;
  tenantId: string;
  name: string;
  status: string;
  metadataJson: string;
  createdAt: string;
  updatedAt: string;
}

export const fetchSupportingMetrics = async (): Promise<SupportingMetric[]> => {
  const res = await fetch(`${BASE_URL}/supporting-metrics`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};

export const fetchSupportingMetric = async (id: string): Promise<SupportingMetric> => {
  const res = await fetch(`${BASE_URL}/supporting-metrics/${id}`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};
