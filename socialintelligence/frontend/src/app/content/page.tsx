'use client';
import { useEffect, useState } from 'react';

export default function Content() {
  const [content, setContent] = useState<any[]>([]);
  const [analysis, setAnalysis] = useState('');

  useEffect(() => {
    fetch('/api/content/top-performing', { headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => setContent(data))
      .catch(console.error);

    fetch('/api/content/analyze-patterns', { headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => setAnalysis(data.analysis))
      .catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Top Content & Patterns</h1>

      <div className="bg-purple-50 p-6 rounded shadow mb-8 border border-purple-200">
        <h2 className="text-lg font-bold text-purple-800 mb-2">AI Pattern Analysis</h2>
        <p className="text-gray-700">{analysis || 'Loading analysis...'}</p>
      </div>

      <div className="grid grid-cols-1 gap-4">
        {content.map(c => (
          <div key={c.id} className="bg-white p-4 rounded shadow flex justify-between items-center">
            <div>
              <p className="font-semibold">{c.contentType}</p>
              <p className="text-sm text-gray-500">Likes: {c.likes} | Views: {c.views}</p>
            </div>
            <a href={c.contentUrl} target="_blank" className="text-blue-500 hover:underline">View</a>
          </div>
        ))}
      </div>
    </div>
  );
}
