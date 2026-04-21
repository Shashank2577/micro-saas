"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../../lib/api';
import CitationCard from '../../components/CitationCard';

export default function CitationsPage() {
  const [citations, setCitations] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [filterType, setFilterType] = useState('all');

  useEffect(() => {
    const fetchCitations = async () => {
      try {
        const data = await api.getCitations();
        setCitations(data || []);
      } catch (error) {
        console.error("Error fetching citations", error);
      } finally {
        setLoading(false);
      }
    };
    fetchCitations();
  }, []);

  const filteredCitations = filterType === 'all'
    ? citations
    : citations.filter(c => c.sourceType?.toLowerCase() === filterType.toLowerCase());

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Source Citations</h1>
      </div>

      <div className="bg-white p-4 rounded-lg shadow flex space-x-4">
        <label className="flex items-center space-x-2">
          <span className="text-sm font-medium">Filter by type:</span>
          <select
            value={filterType}
            onChange={(e) => setFilterType(e.target.value)}
            className="border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500 p-2 border"
          >
            <option value="all">All</option>
            <option value="case">Case Law</option>
            <option value="statute">Statute</option>
            <option value="article">Article</option>
          </select>
        </label>
      </div>

      {loading ? (
        <div>Loading citations...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCitations.map(citation => (
            <CitationCard key={citation.id} citation={citation} />
          ))}
          {filteredCitations.length === 0 && (
            <div className="col-span-full text-center text-gray-500 py-8 bg-white rounded-lg shadow">
              No citations found for the selected filter.
            </div>
          )}
        </div>
      )}
    </div>
  );
}
