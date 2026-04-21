"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../../../lib/api';

export default function QueryDetailPage({ params }: { params: { id: string } }) {
  const [query, setQuery] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchQuery = async () => {
      try {
        const data = await api.getQuery(params.id);
        setQuery(data);
      } catch (error) {
        console.error("Error fetching query details", error);
      } finally {
        setLoading(false);
      }
    };
    fetchQuery();
  }, [params.id]);

  if (loading) return <div>Loading query details...</div>;
  if (!query) return <div>Query not found.</div>;

  return (
    <div className="space-y-6">
      <div className="bg-white p-6 rounded-lg shadow">
        <h1 className="text-2xl font-bold mb-2">Query Details</h1>
        <p className="text-lg text-gray-800">{query.text}</p>
        <div className="mt-4 flex space-x-4 text-sm text-gray-500">
          <span>Jurisdiction: {query.jurisdiction}</span>
          <span>Practice Area: {query.practiceArea}</span>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">AI Analysis Results</h2>
          {query.analysis ? (
            <p className="text-gray-700 whitespace-pre-wrap">{query.analysis}</p>
          ) : (
            <p className="text-gray-500">No AI analysis available yet.</p>
          )}
        </div>

        <div className="space-y-6">
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Source Citations</h2>
            {query.citations && query.citations.length > 0 ? (
              <ul className="list-disc pl-5 space-y-2">
                {query.citations.map((cite: any) => (
                  <li key={cite.id} className="text-gray-700">{cite.text}</li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-500">No citations found.</p>
            )}
          </div>

          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Related Precedents</h2>
            {query.precedents && query.precedents.length > 0 ? (
              <ul className="list-disc pl-5 space-y-2">
                {query.precedents.map((prec: any) => (
                  <li key={prec.id} className="text-gray-700">{prec.title}</li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-500">No related precedents found.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
