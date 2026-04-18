"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '../../../lib/api';

export default function NewPolicyPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    policyNumber: '',
    customerName: '',
    policyType: 'AUTO',
    premiumAmount: '',
    startDate: new Date().toISOString().split('T')[0],
    endDate: new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toISOString().split('T')[0],
  });
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const payload = {
        ...formData,
        premiumAmount: parseFloat(formData.premiumAmount),
        startDate: new Date(formData.startDate).toISOString(),
        endDate: new Date(formData.endDate).toISOString(),
      };
      await api.post('/policies', payload, '00000000-0000-0000-0000-000000000000');
      router.push('/policies');
    } catch (error) {
      console.error("Failed to submit policy", error);
      alert("Error creating policy.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">Create New Policy</h1>
      
      <form onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
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
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="customerName">
            Customer Name
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
            id="customerName" 
            type="text" 
            required
            value={formData.customerName}
            onChange={(e) => setFormData({...formData, customerName: e.target.value})}
          />
        </div>

        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="policyType">
            Policy Type
          </label>
          <select 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            id="policyType"
            value={formData.policyType}
            onChange={(e) => setFormData({...formData, policyType: e.target.value})}
          >
            <option value="AUTO">Auto</option>
            <option value="HOME">Home</option>
            <option value="LIFE">Life</option>
            <option value="HEALTH">Health</option>
          </select>
        </div>

        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="premiumAmount">
            Premium Amount ($)
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
            id="premiumAmount" 
            type="number" 
            step="0.01"
            required
            value={formData.premiumAmount}
            onChange={(e) => setFormData({...formData, premiumAmount: e.target.value})}
          />
        </div>

        <div className="mb-4 flex gap-4">
          <div className="flex-1">
            <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="startDate">
              Start Date
            </label>
            <input 
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
              id="startDate" 
              type="date" 
              required
              value={formData.startDate}
              onChange={(e) => setFormData({...formData, startDate: e.target.value})}
            />
          </div>
          <div className="flex-1">
            <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="endDate">
              End Date
            </label>
            <input 
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
              id="endDate" 
              type="date" 
              required
              value={formData.endDate}
              onChange={(e) => setFormData({...formData, endDate: e.target.value})}
            />
          </div>
        </div>

        <div className="flex items-center justify-between mt-6">
          <button 
            className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline disabled:opacity-50" 
            type="submit"
            disabled={submitting}
          >
            {submitting ? 'Creating...' : 'Create Policy'}
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
