const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090/api/v1/narratives';

export interface NarrativeSection {
  id: string;
  tenantId: string;
  name: string;
  status: string;
  metadataJson: string;
  createdAt: string;
  updatedAt: string;
}

export const fetchNarrativeSections = async (): Promise<NarrativeSection[]> => {
  const res = await fetch(`${BASE_URL}/narrative-sections`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};

export const fetchNarrativeSection = async (id: string): Promise<NarrativeSection> => {
  const res = await fetch(`${BASE_URL}/narrative-sections/${id}`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};
