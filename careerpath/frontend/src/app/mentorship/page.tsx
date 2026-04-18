'use client';

import React, { useState } from 'react';
import { api } from '@/lib/api';

export default function MentorshipPage() {
  const [goals, setGoals] = useState('');
  const [match, setMatch] = useState('');
  const [loading, setLoading] = useState(false);

  const findMatch = () => {
    setLoading(true);
    api.fetchWithTenant('/mentors/match', {
      method: 'POST',
      body: JSON.stringify({ careerGoals: goals }),
    })
      .then(data => setMatch(data.match))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-6">Mentorship Matching</h1>
      <div className="mb-4">
        <label className="block text-gray-700 mb-2">What are your career goals?</label>
        <textarea 
          className="w-full p-3 border rounded h-32"
          value={goals}
          onChange={(e) => setGoals(e.target.value)}
          placeholder="e.g., I want to transition into a management role..."
        />
      </div>
      <button 
        className="bg-blue-600 text-white px-4 py-2 rounded mb-6"
        onClick={findMatch}
        disabled={loading || !goals}
      >
        {loading ? 'Finding Match...' : 'Find Mentor Match'}
      </button>

      {match && (
        <div className="bg-white p-6 border rounded shadow-sm whitespace-pre-wrap">
          <h2 className="font-semibold mb-2">Mentor Match Suggestion:</h2>
          {match}
        </div>
      )}
    </div>
  );
}
