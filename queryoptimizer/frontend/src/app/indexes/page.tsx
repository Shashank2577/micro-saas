"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';

export default function IndexesPage() {
  const [indexes, setIndexes] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/indexes')
      .then(res => setIndexes(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Index Advisor</h1>
      <div className="bg-white border rounded-lg shadow overflow-hidden">
        <table className="w-full text-left text-sm text-gray-500">
          <thead className="bg-gray-50 text-gray-700">
            <tr>
              <th className="px-6 py-3 border-b">Table</th>
              <th className="px-6 py-3 border-b">Columns</th>
              <th className="px-6 py-3 border-b">Creation Statement</th>
              <th className="px-6 py-3 border-b">Improvement</th>
            </tr>
          </thead>
          <tbody>
            {indexes.map(idx => (
              <tr key={idx.id} className="border-b hover:bg-gray-50">
                <td className="px-6 py-4 font-semibold">{idx.tableName}</td>
                <td className="px-6 py-4">{idx.columnsSuggested}</td>
                <td className="px-6 py-4 font-mono text-xs">{idx.creationStatement}</td>
                <td className="px-6 py-4 text-green-600 font-bold">{(idx.estimatedImprovement * 100).toFixed(1)}%</td>
              </tr>
            ))}
            {indexes.length === 0 && (
              <tr>
                <td colSpan={4} className="px-6 py-4 text-center">No index suggestions found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
