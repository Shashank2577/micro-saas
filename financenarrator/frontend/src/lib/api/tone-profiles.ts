const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090/api/v1/narratives';

export interface ToneProfile {
  id: string;
  tenantId: string;
  name: string;
  status: string;
  metadataJson: string;
  createdAt: string;
  updatedAt: string;
}

export const fetchToneProfiles = async (): Promise<ToneProfile[]> => {
  const res = await fetch(`${BASE_URL}/tone-profiles`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};

export const fetchToneProfile = async (id: string): Promise<ToneProfile> => {
  const res = await fetch(`${BASE_URL}/tone-profiles/${id}`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};
