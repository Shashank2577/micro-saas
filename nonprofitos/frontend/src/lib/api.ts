const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export const fetchDonors = async () => {
  const res = await fetch(`${BASE_URL}/donors`, {
    headers: {
      'X-Tenant-ID': 'test-tenant'
    }
  });
  if (!res.ok) throw new Error('Failed to fetch donors');
  return res.json();
};

export const fetchGrants = async () => {
  const res = await fetch(`${BASE_URL}/grants`, {
    headers: {
      'X-Tenant-ID': 'test-tenant'
    }
  });
  if (!res.ok) throw new Error('Failed to fetch grants');
  return res.json();
};

export const fetchImpacts = async () => {
  const res = await fetch(`${BASE_URL}/impacts`, {
    headers: {
      'X-Tenant-ID': 'test-tenant'
    }
  });
  if (!res.ok) throw new Error('Failed to fetch impacts');
  return res.json();
};
