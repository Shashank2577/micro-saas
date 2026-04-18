"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';

export default function PricingPage() {
  const [recommendations, setRecommendations] = useState<any[]>([]);

  const load = async () => {
    try {
      const res = await api.get('/api/pricing-recommendations');
      setRecommendations(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const applyRecommendation = async (id: string) => {
    try {
      await api.put(`/api/pricing-recommendations/${id}/apply`);
      load(); // Reload list
    } catch(err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Pending Pricing Actions</h1>

      {recommendations.length === 0 ? (
        <p>No pending recommendations.</p>
      ) : (
        <div className="space-y-4">
          {recommendations.map(rec => (
            <div key={rec.id} className="border p-4 rounded shadow bg-white">
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="font-bold">SKU ID: {rec.skuId}</h3>
                  <p className="text-lg">Recommended Price: <span className="font-bold text-green-600">${rec.recommendedPrice}</span></p>
                  <p className="text-sm text-gray-600">Margin: {rec.marginPercentage}%</p>
                  <p className="mt-2"><strong>AI Reasoning:</strong> {rec.reasoning}</p>
                </div>
                <button
                  onClick={() => applyRecommendation(rec.id)}
                  className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
                >
                  Apply Price
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
