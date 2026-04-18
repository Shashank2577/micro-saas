"use client";

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { api } from '../../lib/api';

export default function PoliciesPage() {
  const [policies, setPolicies] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPolicies = async () => {
      try {
        const data = await api.get('/policies', '00000000-0000-0000-0000-000000000000');
        setPolicies(data);
      } catch (error) {
        console.error("Failed to fetch policies", error);
      } finally {
        setLoading(false);
      }
    };
    fetchPolicies();
  }, []);

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">Policies</h1>
        <Link href="/policies/new" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded">
          New Policy
        </Link>
      </div>

      {loading ? (
        <div className="text-center p-8">Loading policies...</div>
      ) : policies.length === 0 ? (
        <div className="bg-white p-6 rounded shadow text-center text-gray-500">
          No policies found.
        </div>
      ) : (
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <ul className="divide-y divide-gray-200">
            {policies.map((policy) => (
              <li key={policy.id}>
                <Link href={`/policies/${policy.id}`} className="block hover:bg-gray-50">
                  <div className="px-4 py-4 sm:px-6">
                    <div className="flex items-center justify-between">
                      <p className="text-sm font-medium text-indigo-600 truncate">
                        {policy.policyNumber} - {policy.customerName}
                      </p>
                      <div className="ml-2 flex-shrink-0 flex">
                        <p className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                          {policy.policyType}
                        </p>
                      </div>
                    </div>
                    <div className="mt-2 sm:flex sm:justify-between">
                      <div className="sm:flex">
                        <p className="flex items-center text-sm text-gray-500">
                          Premium: ${policy.premiumAmount}
                        </p>
                      </div>
                      <div className="mt-2 flex items-center text-sm text-gray-500 sm:mt-0">
                        <p>
                          Valid: {new Date(policy.startDate).toLocaleDateString()} to {new Date(policy.endDate).toLocaleDateString()}
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
