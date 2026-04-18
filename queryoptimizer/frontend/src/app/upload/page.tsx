"use client";

import { useState } from 'react';
import api from '@/lib/api';

export default function UploadPage() {
  const [jsonText, setJsonText] = useState('');
  const [loading, setLoading] = useState(false);

  const handleUpload = () => {
    try {
      const data = JSON.parse(jsonText);
      setLoading(true);
      api.post('/api/queries/upload', data)
        .then(() => {
          alert('Upload successful');
          setJsonText('');
        })
        .catch(err => {
          console.error(err);
          alert('Upload failed');
        })
        .finally(() => setLoading(false));
    } catch (e) {
      alert('Invalid JSON');
    }
  };

  return (
    <div className="max-w-3xl">
      <h1 className="text-2xl font-bold mb-6">Upload Slow Query Logs</h1>
      <div className="bg-white p-6 rounded-lg shadow border flex flex-col gap-4">
        <p className="text-gray-600 text-sm">Paste your slow query logs as a JSON array of `QueryLogEntry` objects.</p>
        <textarea
          value={jsonText}
          onChange={(e) => setJsonText(e.target.value)}
          rows={15}
          className="w-full border rounded p-4 font-mono text-sm"
          placeholder='[{"query": "SELECT * FROM users", "executionTimeMs": 150.0, "databaseType": "PostgreSQL"}]'
        />
        <div className="flex justify-end">
          <button
            onClick={handleUpload}
            disabled={loading || !jsonText}
            className="bg-blue-600 text-white px-6 py-2 rounded shadow hover:bg-blue-700 disabled:bg-gray-400"
          >
            {loading ? 'Uploading...' : 'Submit Logs'}
          </button>
        </div>
      </div>
    </div>
  );
}
