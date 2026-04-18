'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function NewSubscriptionPage() {
  const router = useRouter();
  const [planId, setPlanId] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // Real implementation would POST to /api/subscriptions
    router.push('/subscriptions');
  };

  return (
    <div className="p-8 max-w-lg mx-auto">
      <h1 className="text-3xl font-bold mb-6">Create Subscription</h1>
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow border">
        <div className="mb-4">
          <label className="block text-gray-700 mb-2">Plan ID</label>
          <input 
            type="text" 
            value={planId} 
            onChange={(e) => setPlanId(e.target.value)} 
            className="w-full border p-2 rounded" 
            required 
            data-testid="plan-input"
          />
        </div>
        <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded">Subscribe</button>
      </form>
    </div>
  );
}
