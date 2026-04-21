"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../../lib/api';

export default function PrecedentsPage() {
  const [precedents, setPrecedents] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [jurisdictionFilter, setJurisdictionFilter] = useState('all');

  useEffect(() => {
    const fetchPrecedents = async () => {
      try {
        const data = await api.getPrecedents();
        setPrecedents(data || []);
      } catch (error) {
        console.error("Error fetching precedents", error);
      } finally {
        setLoading(false);
      }
    };
    fetchPrecedents();
  }, []);

  const filteredPrecedents = jurisdictionFilter === 'all'
    ? precedents
    : precedents.filter(p => p.jurisdiction?.toLowerCase() === jurisdictionFilter.toLowerCase());

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Precedent Notes</h1>
      </div>

      <div className="bg-white p-4 rounded-lg shadow flex space-x-4">
        <label className="flex items-center space-x-2">
          <span className="text-sm font-medium">Jurisdiction:</span>
          <select
            value={jurisdictionFilter}
            onChange={(e) => setJurisdictionFilter(e.target.value)}
            className="border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500 p-2 border"
          >
            <option value="all">All Jurisdictions</option>
            <option value="federal">Federal</option>
            <option value="state">State</option>
            <option value="international">International</option>
          </select>
        </label>
      </div>

      {loading ? (
        <div>Loading precedents...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {filteredPrecedents.map(precedent => (
            <div key={precedent.id} className="bg-white p-6 rounded-lg shadow border border-gray-200">
              <h2 className="text-xl font-bold text-indigo-600 mb-2">{precedent.title}</h2>
              <div className="text-sm text-gray-500 mb-4 flex space-x-4">
                <span>Jurisdiction: {precedent.jurisdiction}</span>
                {precedent.court && <span>Court: {precedent.court}</span>}
              </div>
              <p className="text-gray-700">{precedent.summary || precedent.text}</p>
            </div>
          ))}
          {filteredPrecedents.length === 0 && (
            <div className="col-span-full text-center text-gray-500 py-8 bg-white rounded-lg shadow">
              No precedents found.
            </div>
          )}
        </div>
      )}
    </div>
  );
}
