const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8144/api';
const TENANT_ID = 'test-tenant-id';

export async function fetchCompetitors() {
  const res = await fetch(`${BASE_URL}/competitors`, {
    headers: { 'X-Tenant-ID': TENANT_ID }
  });
  if (!res.ok) throw new Error('Failed to fetch competitors');
  return res.json();
}

export async function addCompetitor(competitor: any) {
  const res = await fetch(`${BASE_URL}/competitors`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-Tenant-ID': TENANT_ID
    },
    body: JSON.stringify(competitor)
  });
  if (!res.ok) throw new Error('Failed to add competitor');
  return res.json();
}

export async function deleteCompetitor(id: string) {
  const res = await fetch(`${BASE_URL}/competitors/${id}`, {
    method: 'DELETE',
    headers: { 'X-Tenant-ID': TENANT_ID }
  });
  if (!res.ok) throw new Error('Failed to delete competitor');
}
