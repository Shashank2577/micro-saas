export const fetchApi = async (path: string, options: RequestInit = {}) => {
  const tenantId = '00000000-0000-0000-0000-000000000001';
  const headers = {
    'Content-Type': 'application/json',
    'X-Tenant-ID': tenantId,
    ...options.headers,
  };
  const response = await fetch(`/api/v1/onboardflow/${path}`, { ...options, headers });
  if (!response.ok) {
    throw new Error(`API error: ${response.statusText}`);
  }
  return response.json();
};
