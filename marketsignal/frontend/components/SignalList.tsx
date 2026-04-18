"use client";
import { useEffect, useState } from 'react';
import { fetchApi } from '../lib/api';

export default function SignalList() {
  const [signals, setSignals] = useState<any[]>([]);

  useEffect(() => {
    fetchApi('/signals')
      .then(setSignals)
      .catch(console.error);
  }, []);

  return (
    <div className="space-y-4">
      {signals.map((signal) => (
        <div key={signal.id} className="p-4 border rounded shadow-sm bg-white">
          <h3 className="font-bold text-lg">{signal.title}</h3>
          <p className="text-gray-600 text-sm mb-2">{new Date(signal.publishedAt).toLocaleDateString()}</p>
          <p className="text-sm">{signal.content}</p>
          <div className="mt-2 flex space-x-4">
            <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">Strength: {signal.signalStrength}</span>
            <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded">{signal.sentiment}</span>
          </div>
        </div>
      ))}
      {signals.length === 0 && <p>No signals found.</p>}
    </div>
  );
}
