"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../../lib/api';

export default function QueriesPage() {
  const [queries, setQueries] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');

  useEffect(() => {
    const fetchQueries = async () => {
      try {
        const data = await api.getQueries();
        setQueries(data || []);
      } catch (error) {
        console.error("Error fetching queries", error);
      } finally {
        setLoading(false);
      }
    };
    fetchQueries();
  }, []);

  const filteredQueries = queries.filter(q =>
    (q.text || '').toLowerCase().includes(search.toLowerCase()) ||
    (q.jurisdiction || '').toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Research Queries</h1>
        <button className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700">
          New Query
        </button>
      </div>

      <div className="bg-white p-4 rounded-lg shadow">
        <input
          type="text"
          placeholder="Search queries..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full p-2 border border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500"
        />
      </div>

      {loading ? (
        <div>Loading...</div>
      ) : (
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <ul className="divide-y divide-gray-200">
            {filteredQueries.map(query => (
              <li key={query.id} className="p-4 hover:bg-gray-50">
                <a href={`/queries/${query.id}`} className="block">
                  <p className="text-lg font-medium text-indigo-600">{query.text}</p>
                  <p className="text-sm text-gray-500 mt-1">
                    Jurisdiction: {query.jurisdiction} | Practice Area: {query.practiceArea}
                  </p>
                </a>
              </li>
            ))}
            {filteredQueries.length === 0 && (
              <li className="p-4 text-gray-500">No queries found.</li>
            )}
          </ul>
        </div>
      )}
    </div>
  );
}
