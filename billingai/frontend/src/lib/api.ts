const API_BASE = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8154/api';

export async function getInvoices() {
  const res = await fetch(`${API_BASE}/invoices`, { 
    cache: 'no-store',
    headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000001' }
  });
  if (!res.ok) throw new Error('Failed to fetch invoices');
  return res.json();
}

export async function getSubscriptions() {
  const res = await fetch(`${API_BASE}/subscriptions`, { 
    cache: 'no-store',
    headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000001' }
  });
  if (!res.ok) throw new Error('Failed to fetch subscriptions');
  return res.json();
}
