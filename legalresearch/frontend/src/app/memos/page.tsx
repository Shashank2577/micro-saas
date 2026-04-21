"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../../lib/api';

export default function MemosPage() {
  const [memos, setMemos] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMemos = async () => {
      try {
        const data = await api.getMemos();
        setMemos(data || []);
      } catch (error) {
        console.error("Error fetching memos", error);
      } finally {
        setLoading(false);
      }
    };
    fetchMemos();
  }, []);

  const getStatusColor = (status: string) => {
    switch (status?.toLowerCase()) {
      case 'draft': return 'bg-gray-100 text-gray-800';
      case 'review': return 'bg-yellow-100 text-yellow-800';
      case 'approved': return 'bg-green-100 text-green-800';
      default: return 'bg-blue-100 text-blue-800';
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Memo Drafts</h1>
        <button className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700">
          New Memo
        </button>
      </div>

      {loading ? (
        <div>Loading memos...</div>
      ) : (
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <ul className="divide-y divide-gray-200">
            {memos.map((memo) => (
              <li key={memo.id}>
                <a href={`/memos/${memo.id}`} className="block hover:bg-gray-50">
                  <div className="px-4 py-4 sm:px-6 flex items-center justify-between">
                    <div className="truncate">
                      <p className="text-sm font-medium text-indigo-600 truncate">
                        {memo.title || 'Untitled Memo'}
                      </p>
                    </div>
                    <div className="ml-2 flex-shrink-0 flex">
                      <p className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(memo.status)}`}>
                        {memo.status || 'Draft'}
                      </p>
                    </div>
                  </div>
                </a>
              </li>
            ))}
            {memos.length === 0 && (
              <li className="px-4 py-4 sm:px-6 text-gray-500 text-center">
                No memo drafts found.
              </li>
            )}
          </ul>
        </div>
      )}
    </div>
  );
}
