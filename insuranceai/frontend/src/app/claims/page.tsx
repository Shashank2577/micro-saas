"use client";

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { api } from '../../lib/api';

export default function ClaimsPage() {
  const [claims, setClaims] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchClaims = async () => {
      try {
        const data = await api.get('/claims', '00000000-0000-0000-0000-000000000000');
        setClaims(data);
      } catch (error) {
        console.error("Failed to fetch claims", error);
      } finally {
        setLoading(false);
      }
    };
    fetchClaims();
  }, []);

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">Claims</h1>
        <Link href="/claims/new" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded">
          New Claim
        </Link>
      </div>

      {loading ? (
        <div className="text-center p-8">Loading claims...</div>
      ) : claims.length === 0 ? (
        <div className="bg-white p-6 rounded shadow text-center text-gray-500">
          No claims found.
        </div>
      ) : (
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <ul className="divide-y divide-gray-200">
            {claims.map((claim) => (
              <li key={claim.id}>
                <Link href={`/claims/${claim.id}`} className="block hover:bg-gray-50">
                  <div className="px-4 py-4 sm:px-6">
                    <div className="flex items-center justify-between">
                      <p className="text-sm font-medium text-indigo-600 truncate">
                        Claim {claim.claimNumber}
                      </p>
                      <div className="ml-2 flex-shrink-0 flex">
                        <p className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                          claim.status === 'NEW' ? 'bg-yellow-100 text-yellow-800' : 
                          claim.status === 'APPROVED' ? 'bg-green-100 text-green-800' : 
                          'bg-red-100 text-red-800'
                        }`}>
                          {claim.status}
                        </p>
                      </div>
                    </div>
                    <div className="mt-2 sm:flex sm:justify-between">
                      <div className="sm:flex">
                        <p className="flex items-center text-sm text-gray-500">
                          Policy: {claim.policyNumber}
                        </p>
                      </div>
                      <div className="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
                        <p>
                          Amount: ${claim.amount}
                        </p>
                      </div>
                    </div>
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
