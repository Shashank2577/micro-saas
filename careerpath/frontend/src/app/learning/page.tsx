'use client';

import React, { useState } from 'react';
import { api } from '@/lib/api';

const MOCK_EMP_ID = '00000000-0000-0000-0000-000000000001';

export default function LearningPage() {
  const [learningPaths, setLearningPaths] = useState('');
  const [loading, setLoading] = useState(false);

  const generatePaths = () => {
    setLoading(true);
    api.getLearningPaths(MOCK_EMP_ID)
      .then(data => setLearningPaths(data.learningPaths))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-6">AI Learning Paths</h1>
      <button 
        className="bg-green-600 text-white px-4 py-2 rounded mb-6"
        onClick={generatePaths}
        disabled={loading}
      >
        {loading ? 'Curating...' : 'Curate Learning Paths'}
      </button>

      {learningPaths && (
        <div className="bg-white p-6 border rounded shadow-sm whitespace-pre-wrap">
          {learningPaths}
        </div>
      )}
    </div>
  );
}
