'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';

export default function ContentPage() {
  const [contents, setContents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/api/v1/content', {
      headers: {
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
      }
    })
      .then(res => res.json())
      .then(data => {
        setContents(data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  return (
    <div className="p-8 max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">Content Items</h1>
        <Link href="/" className="text-blue-600 hover:underline">Back to Dashboard</Link>
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : contents.length === 0 ? (
        <div className="bg-white p-8 rounded-lg shadow-sm border border-gray-200 text-center">
          <p className="text-gray-600 mb-4">No content items found.</p>
        </div>
      ) : (
        <div className="grid gap-4">
          {contents.map((item: any) => (
            <div key={item.id} className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 flex justify-between items-center">
              <div>
                <h3 className="text-xl font-semibold mb-1">{item.title}</h3>
                <div className="flex space-x-4 text-sm text-gray-500">
                  <span className="bg-gray-100 px-2 py-1 rounded">{item.type}</span>
                  <span className={`px-2 py-1 rounded ${
                    item.status === 'PUBLISHED' ? 'bg-green-100 text-green-800' :
                    item.status === 'REVIEW' ? 'bg-yellow-100 text-yellow-800' :
                    'bg-blue-100 text-blue-800'
                  }`}>
                    {item.status}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
