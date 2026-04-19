const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090/api/v1/narratives';

export interface ApprovalReview {
  id: string;
  tenantId: string;
  name: string;
  status: string;
  metadataJson: string;
  createdAt: string;
  updatedAt: string;
}

export const fetchApprovalReviews = async (): Promise<ApprovalReview[]> => {
  const res = await fetch(`${BASE_URL}/approval-reviews`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};

export const fetchApprovalReview = async (id: string): Promise<ApprovalReview> => {
  const res = await fetch(`${BASE_URL}/approval-reviews/${id}`, {
    headers: { 'X-Tenant-ID': 'default-tenant' }
  });
  if (!res.ok) throw new Error('Failed to fetch');
  return res.json();
};
