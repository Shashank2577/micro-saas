"use client";
import { useEffect, useState } from 'react';
import { fetchApi } from '../lib/api';

export default function PatternList() {
  const [patterns, setPatterns] = useState<any[]>([]);

  useEffect(() => {
    fetchApi('/patterns')
      .then(setPatterns)
      .catch(console.error);
  }, []);

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      {patterns.map((pattern) => (
        <div key={pattern.id} className="p-4 border rounded shadow-sm bg-gray-50 border-l-4 border-l-indigo-500">
          <h3 className="font-bold text-lg">{pattern.title}</h3>
          <span className="text-xs font-semibold text-indigo-600 uppercase tracking-wider">{pattern.patternType}</span>
          <p className="mt-2 text-sm text-gray-700">{pattern.description}</p>
        </div>
      ))}
      {patterns.length === 0 && <p>No patterns detected yet.</p>}
    </div>
  );
}
