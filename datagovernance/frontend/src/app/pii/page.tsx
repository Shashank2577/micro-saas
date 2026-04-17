'use client';
import { useState } from 'react';
import { api } from '@/lib/api';

export default function PiiDetectionPage() {
  const [text, setText] = useState('');
  const [results, setResults] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  const handleDetect = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await api.pii.detect(text);
      setResults(res.piiTypes);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">PII Detection Tool</h1>
      
      <div className="bg-white p-6 rounded-lg shadow-sm border mb-8">
        <form onSubmit={handleDetect}>
          <label className="block text-sm font-medium mb-2">Text to Analyze</label>
          <textarea 
            required 
            value={text} 
            onChange={e => setText(e.target.value)} 
            className="w-full border rounded p-3 min-h-[150px] mb-4" 
            placeholder="Paste text here to detect sensitive information..."
          />
          <button disabled={loading} type="submit" className="bg-purple-600 text-white px-6 py-2 rounded hover:bg-purple-700 disabled:opacity-50">
            {loading ? 'Analyzing...' : 'Detect PII'}
          </button>
        </form>
      </div>

      {results.length > 0 && (
        <div className="bg-white p-6 rounded-lg shadow-sm border">
          <h2 className="text-xl font-semibold mb-4">Detected PII Types</h2>
          <div className="flex gap-2">
            {results.map(type => (
              <span key={type} className="bg-red-100 text-red-800 px-3 py-1 rounded-full font-medium">
                {type}
              </span>
            ))}
          </div>
        </div>
      )}
       {!loading && text && results.length === 0 && (
          <div className="text-gray-500 mt-4">No PII detected in the previous check.</div>
       )}
    </div>
  );
}
