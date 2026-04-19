'use client';
import { useEffect, useState } from 'react';
import { fetchApprovalReviews, ApprovalReview } from '../../lib/api/approval-reviews';
import Link from 'next/link';

export default function ApprovalReviewsPage() {
  const [data, setData] = useState<ApprovalReview[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    fetchApprovalReviews()
      .then(setData)
      .catch(setError)
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Approval Reviews</h1>

      <div className="mb-8 p-4 border rounded bg-gray-50">
        <h2 className="text-xl font-bold mb-2">Create New Approval Review</h2>
        <form className="flex flex-col gap-4 max-w-md">
            <input type="text" placeholder="Name" className="p-2 border rounded" required />
            <select className="p-2 border rounded">
                <option value="DRAFT">Draft</option>
                <option value="VALIDATED">Validated</option>
            </select>
            <button type="submit" className="bg-blue-500 text-white p-2 rounded">Create</button>
        </form>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div>Error loading approval reviews</div>}
      {!loading && !error && data.length === 0 && <div>No approval reviews found.</div>}

      {!loading && !error && data.length > 0 && (
        <ul>
          {data.map(item => (
            <li key={item.id} className="mb-2 p-4 border rounded">
              <Link href={`/approval-reviews/${item.id}`}>{item.name}</Link> - {item.status}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
