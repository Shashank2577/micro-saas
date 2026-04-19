const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090/api/v1/narratives';

export interface ExportArtifact {
  id: string;
  tenantId: string;
  name: string;
  status: string;
  metadataJson: string;
  createdAt: string;
  updatedAt: string;
}

export const fetchExportArtifacts = async (): Promise<ExportArtifact[]> => {
  const res = await fetch(`${BASE_URL}/export-artifacts`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};

export const fetchExportArtifact = async (id: string): Promise<ExportArtifact> => {
  const res = await fetch(`${BASE_URL}/export-artifacts/${id}`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};
