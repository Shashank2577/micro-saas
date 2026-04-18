"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '../../../lib/api';

export default function NewClaimPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    claimNumber: '',
    policyNumber: '',
    description: '',
    amount: '',
    incidentDate: '',
    filedDate: new Date().toISOString().split('T')[0],
  });
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const payload = {
        ...formData,
        amount: parseFloat(formData.amount),
        incidentDate: new Date(formData.incidentDate).toISOString(),
        filedDate: new Date(formData.filedDate).toISOString(),
      };
      await api.post('/claims', payload, '00000000-0000-0000-0000-000000000000');
      router.push('/claims');
    } catch (error) {
      console.error("Failed to submit claim", error);
      alert("Error submitting claim.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">File New Claim</h1>
      
      <form onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="claimNumber">
            Claim Number
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
            id="claimNumber" 
            type="text" 
            required
            value={formData.claimNumber}
            onChange={(e) => setFormData({...formData, claimNumber: e.target.value})}
          />
        </div>
        
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="policyNumber">
            Policy Number
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
            id="policyNumber" 
            type="text" 
            required
            value={formData.policyNumber}
            onChange={(e) => setFormData({...formData, policyNumber: e.target.value})}
          />
        </div>

        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="amount">
            Claim Amount ($)
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
            id="amount" 
            type="number" 
            step="0.01"
            required
            value={formData.amount}
            onChange={(e) => setFormData({...formData, amount: e.target.value})}
          />
        </div>

        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="incidentDate">
            Incident Date
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
            id="incidentDate" 
            type="date" 
            required
            value={formData.incidentDate}
            onChange={(e) => setFormData({...formData, incidentDate: e.target.value})}
          />
        </div>

        <div className="mb-6">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="description">
            Description
          </label>
          <textarea 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline h-32" 
            id="description" 
            required
            value={formData.description}
            onChange={(e) => setFormData({...formData, description: e.target.value})}
          ></textarea>
        </div>

        <div className="flex items-center justify-between">
          <button 
            className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:opacity-50" 
            type="submit"
            disabled={submitting}
          >
            {submitting ? 'Submitting...' : 'Submit Claim'}
          </button>
          <button 
            type="button" 
            onClick={() => router.back()}
            className="inline-block align-baseline font-bold text-sm text-indigo-600 hover:text-indigo-800"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}
