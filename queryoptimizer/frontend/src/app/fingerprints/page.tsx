"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';
import Link from 'next/link';

export default function FingerprintsPage() {
  const [fingerprints, setFingerprints] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/queries/fingerprints')
      .then(res => {
        setFingerprints(res.data.content || []);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Query Fingerprints</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="bg-white border rounded-lg shadow overflow-hidden">
          <table className="w-full text-left text-sm text-gray-500">
            <thead className="bg-gray-50 text-gray-700">
              <tr>
                <th className="px-6 py-3 border-b">ID</th>
                <th className="px-6 py-3 border-b">Query</th>
                <th className="px-6 py-3 border-b">Database</th>
                <th className="px-6 py-3 border-b">Actions</th>
              </tr>
            </thead>
            <tbody>
              {fingerprints.map(fp => (
                <tr key={fp.id} className="border-b hover:bg-gray-50">
                  <td className="px-6 py-4 font-mono text-xs">{fp.id.substring(0, 8)}...</td>
                  <td className="px-6 py-4 truncate max-w-md font-mono text-xs">{fp.normalizedQuery}</td>
                  <td className="px-6 py-4">{fp.databaseType}</td>
                  <td className="px-6 py-4">
                    <Link href={`/fingerprints/${fp.id}`} className="text-blue-600 hover:underline">
                      View Details
                    </Link>
                  </td>
                </tr>
              ))}
              {fingerprints.length === 0 && (
                <tr>
                  <td colSpan={4} className="px-6 py-4 text-center">No fingerprints found. Upload logs to get started.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
