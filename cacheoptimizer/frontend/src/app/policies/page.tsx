"use client";
import { useEffect, useState } from 'react';
import { api, CachePolicy } from '@/src/api/api';
import { PolicyList } from '@/src/components/PolicyList';
import Link from 'next/link';

export default function PoliciesPage() {
  const [policies, setPolicies] = useState<CachePolicy[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getPolicies().then(p => {
      setPolicies(p);
      setLoading(false);
    }).catch(console.error);
  }, []);

  return (
    <div className="p-8 max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Cache Policies</h1>
        <Link href="/policies/new" className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition">
          New Policy
        </Link>
      </div>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <PolicyList policies={policies} />
      )}
    </div>
  );
}
