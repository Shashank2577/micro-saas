const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090/api/v1/narratives';

export interface NarrativeRequest {
  id: string;
  tenantId: string;
  name: string;
  status: string;
  metadataJson: string;
  createdAt: string;
  updatedAt: string;
}

export const fetchNarrativeRequests = async (): Promise<NarrativeRequest[]> => {
  const res = await fetch(`${BASE_URL}/narrative-requests`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};

export const fetchNarrativeRequest = async (id: string): Promise<NarrativeRequest> => {
  const res = await fetch(`${BASE_URL}/narrative-requests/${id}`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};
