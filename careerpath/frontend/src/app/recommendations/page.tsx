'use client';

import React, { useState } from 'react';
import { api } from '@/lib/api';

const MOCK_EMP_ID = '00000000-0000-0000-0000-000000000001';

export default function RecommendationsPage() {
  const [recommendations, setRecommendations] = useState('');
  const [loading, setLoading] = useState(false);

  const generateRecommendations = () => {
    setLoading(true);
    api.getRecommendations(MOCK_EMP_ID)
      .then(data => setRecommendations(data.recommendations))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-6">AI Role Recommendations</h1>
      <button 
        className="bg-purple-600 text-white px-4 py-2 rounded mb-6"
        onClick={generateRecommendations}
        disabled={loading}
      >
        {loading ? 'Generating...' : 'Suggest Next Roles'}
      </button>

      {recommendations && (
        <div className="bg-white p-6 border rounded shadow-sm whitespace-pre-wrap">
          {recommendations}
        </div>
      )}
    </div>
  );
}
