"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';

export default function Sources() {
  const [sources, setSources] = useState<any[]>([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await api.get('/api/sources');
        setSources(response.data);
      } catch (e) {
        console.error(e);
      }
    }
    fetchData();
  }, []);

  async function addSource() {
    try {
        await api.post('/api/sources', {
            name: "Mock API Source",
            type: "API",
            connectionDetails: "{}"
        });
        const response = await api.get('/api/sources');
        setSources(response.data);
    } catch (e) {
        console.error(e);
    }
  }

  return (
    <main className="p-8">
      <h1 className="text-3xl font-bold mb-6">Data Sources</h1>
      <button onClick={addSource} className="bg-blue-500 text-white p-2 mb-4 rounded">Add Mock Source</button>
      <ul>
        {sources.map(s => (
          <li key={s.id} className="border p-2 mb-2">
            {s.name} - {s.type}
          </li>
        ))}
      </ul>
    </main>
  );
}
