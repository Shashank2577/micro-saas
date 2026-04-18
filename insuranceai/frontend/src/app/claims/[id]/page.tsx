"use client";

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import { api } from '../../../lib/api';

export default function ClaimDetailPage() {
  const params = useParams();
  const id = params.id as string;
  const [claim, setClaim] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchClaim = async () => {
      try {
        const data = await api.get(`/claims/${id}`, '00000000-0000-0000-0000-000000000000');
        setClaim(data);
      } catch (error) {
        console.error("Failed to fetch claim", error);
      } finally {
        setLoading(false);
      }
    };
    fetchClaim();
  }, [id]);

  if (loading) return <div className="text-center p-8">Loading claim details...</div>;
  if (!claim) return <div className="text-center p-8 text-red-500">Claim not found</div>;

  return (
    <div className="bg-white shadow overflow-hidden sm:rounded-lg">
      <div className="px-4 py-5 sm:px-6 flex justify-between items-center">
        <div>
          <h3 className="text-lg leading-6 font-medium text-gray-900">Claim Information</h3>
          <p className="mt-1 max-w-2xl text-sm text-gray-500">Details and AI analysis for claim {claim.claimNumber}.</p>
        </div>
        <span className={`px-3 py-1 inline-flex text-sm leading-5 font-semibold rounded-full ${
          claim.status === 'NEW' ? 'bg-yellow-100 text-yellow-800' : 
          claim.status === 'APPROVED' ? 'bg-green-100 text-green-800' : 
          'bg-red-100 text-red-800'
        }`}>
          {claim.status}
        </span>
      </div>
      <div className="border-t border-gray-200">
        <dl>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Policy Number</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">{claim.policyNumber}</dd>
          </div>
          <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Amount</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">${claim.amount}</dd>
          </div>
          <div className="bg-gray-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Incident Date</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">{new Date(claim.incidentDate).toLocaleDateString()}</dd>
          </div>
          <div className="bg-white px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-gray-500">Description</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">{claim.description}</dd>
          </div>
          
          <div className="bg-indigo-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6 border-t border-indigo-100">
            <dt className="text-sm font-medium text-indigo-800">AI Fraud Score</dt>
            <dd className="mt-1 text-sm sm:mt-0 sm:col-span-2 flex items-center">
              <span className={`font-bold text-lg mr-2 ${claim.aiFraudScore > 50 ? 'text-red-600' : 'text-green-600'}`}>
                {claim.aiFraudScore}/100
              </span>
            </dd>
          </div>
          <div className="bg-indigo-50 px-4 py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
            <dt className="text-sm font-medium text-indigo-800">AI Reasoning</dt>
            <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2 italic">
              "{claim.aiFraudReasoning}"
            </dd>
          </div>
        </dl>
      </div>
    </div>
  );
}
