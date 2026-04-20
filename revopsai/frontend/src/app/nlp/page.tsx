"use client";

import { useState } from 'react';
import axios from 'axios';

export default function NlpPage() {
  const [query, setQuery] = useState('');
  const [result, setResult] = useState('');
  const [loading, setLoading] = useState(false);

  const handleAsk = async () => {
    setLoading(true);
    try {
      const res = await axios.post('/api/v1/nlp/query', { query }, {
        headers: { 'X-Tenant-Id': '123e4567-e89b-12d3-a456-426614174000' }
      });
      setResult(res.data.result);
    } catch (e) {
      console.error(e);
      setResult('Error executing query');
    }
    setLoading(false);
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Natural Language Query</h1>
      <div className="flex gap-2 mb-4">
        <input
          className="border p-2 flex-grow text-black"
          value={query}
          onChange={e => setQuery(e.target.value)}
          placeholder="Why did ARR slow in Q1?"
        />
        <button
          className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
          onClick={handleAsk}
          disabled={loading || !query}
        >
          {loading ? 'Asking...' : 'Ask'}
        </button>
      </div>
      {result && (
        <div className="p-4 bg-gray-100 rounded text-black">
          {result}
        </div>
      )}
    </div>
  );
}
