"use client";

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import { api } from '../../../lib/api';

export default function PolicyDetailPage() {
  const params = useParams();
  const id = params.id as string;
  const [policy, setPolicy] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPolicy = async () => {
      try {
        const data = await api.get(`/policies/${id}`, '00000000-0000-0000-0000-000000000000');
        setPolicy(data);
      } catch (error) {
        console.error("Failed to fetch policy", error);
      } finally {
        setLoading(false);
      }
    };
    fetchPolicy();
  }, [id]);

  if (loading) return <div className="text-center p-8">Loading policy details...</div>;
  if (!policy) return <div className="text-center p-8 text-red-500">Policy not found</div>;

  return (
    <div className="bg-white shadow overflow-hidden sm:rounded-lg">
      <div className="px-4 py-5 sm:px-6 flex justify-between items-center">
        <div>
          <h3 className="text-lg leading-6 font-medium text-gray-900">Policy Information</h3>
          <p className="mt-1 max-w-2xl text-sm text-gray-500">Details and AI risk analysis for policy {policy.policyNumber}.</p>
        </div>
        <span className="px-3 py-1 inline-flex text-sm leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
          {policy.policyType}
        </span>
      </div>
      <div className="border-t border-gray-200">
        <dl>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Customer Name</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">{policy.customerName}</dd>
          </div>
          <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Premium</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">${policy.premiumAmount}</dd>
          </div>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Coverage Period</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
              {new Date(policy.startDate).toLocaleDateString()} to {new Date(policy.endDate).toLocaleDateString()}
            </dd>
          </div>
          
          <div className="bg-indigo-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6 border-t border-indigo-100">
            <dt className="text-sm font-medium text-indigo-800">AI Risk Score</dt>
            <dd className="mt-1 text-sm sm:mt-0 sm:col-span-2 flex items-center">
              <span className={`font-bold text-lg mr-2 ${policy.aiRiskScore > 50 ? 'text-red-600' : 'text-green-600'}`}>
                {policy.aiRiskScore}/100
              </span>
            </dd>
          </div>
          <div className="bg-indigo-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-indigo-800">AI Risk Factors</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2 italic">
              "{policy.aiRiskFactors}"
            </dd>
          </div>
        </dl>
      </div>
    </div>
  );
}
