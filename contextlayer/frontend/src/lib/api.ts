export const fetchContext = async (customerId: string) => {
  const res = await fetch(`/api/customers/${customerId}/context`);
  if (!res.ok) throw new Error('Failed to fetch context');
  return res.json();
};

export const fetchVersions = async (customerId: string) => {
  const res = await fetch(`/api/customers/${customerId}/context/versions`);
  if (!res.ok) throw new Error('Failed to fetch versions');
  return res.json();
};
