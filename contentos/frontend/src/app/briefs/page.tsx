'use client';

import { useState } from 'react';
import Link from 'next/link';

export default function BriefsPage() {
  const [formData, setFormData] = useState({ title: '', strategicGoal: '', targetAudience: '' });
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<any>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await fetch('/api/v1/briefs/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
        },
        body: JSON.stringify(formData)
      });
      const data = await res.json();
      setResult(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">Generate AI Content Brief</h1>
        <Link href="/" className="text-blue-600 hover:underline">Back to Dashboard</Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
              <input
                type="text"
                required
                className="w-full border border-gray-300 rounded-md p-2"
                value={formData.title}
                onChange={e => setFormData({...formData, title: e.target.value})}
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Strategic Goal</label>
              <textarea
                required
                className="w-full border border-gray-300 rounded-md p-2 h-24"
                value={formData.strategicGoal}
                onChange={e => setFormData({...formData, strategicGoal: e.target.value})}
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Target Audience</label>
              <textarea
                required
                className="w-full border border-gray-300 rounded-md p-2 h-24"
                value={formData.targetAudience}
                onChange={e => setFormData({...formData, targetAudience: e.target.value})}
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? 'Generating...' : 'Generate Brief'}
            </button>
          </form>
        </div>

        {result && (
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
            <h2 className="text-xl font-bold mb-4 text-green-700">Generated Brief</h2>
            <div className="space-y-4">
              <div>
                <h3 className="font-semibold text-gray-700">Key Messages</h3>
                <ul className="list-disc pl-5 mt-1">
                  {result.keyMessages?.map((msg: string, i: number) => (
                    <li key={i} className="text-sm text-gray-600">{msg}</li>
                  ))}
                </ul>
              </div>
              <div>
                <h3 className="font-semibold text-gray-700">Keywords</h3>
                <div className="flex flex-wrap gap-2 mt-1">
                  {result.keywords?.map((kw: string, i: number) => (
                    <span key={i} className="bg-gray-100 text-gray-700 text-xs px-2 py-1 rounded">
                      {kw}
                    </span>
                  ))}
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
