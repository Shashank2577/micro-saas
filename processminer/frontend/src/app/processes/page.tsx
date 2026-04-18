"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';
import Link from 'next/link';

export default function ProcessesPage() {
  const [processes, setProcesses] = useState<any[]>([]);
  const [systemType, setSystemType] = useState('');
  const [message, setMessage] = useState('');

  const fetchProcesses = async () => {
    try {
      const res = await api.get('/api/process-models');
      setProcesses(res.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchProcesses();
  }, []);

  const handleDiscover = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/api/process-models/discover', { systemType });
      setMessage('Discovery job started. Please refresh shortly.');
      setSystemType('');
      setTimeout(fetchProcesses, 2000);
    } catch (error) {
      setMessage('Error starting discovery');
    }
  };

  return (
    <div className="container mx-auto p-4">
      <Link href="/" className="text-blue-500 mb-4 inline-block">&larr; Back to Dashboard</Link>
      <h1 className="text-2xl font-bold mb-4">Process Models</h1>
      
      <div className="mb-8 bg-gray-100 p-4 rounded">
        <h2 className="font-semibold mb-2">Discover New Process</h2>
        <form onSubmit={handleDiscover} className="flex gap-4 items-center">
          <input className="shadow border rounded py-2 px-3 text-gray-700" placeholder="System Type (e.g. ERP)" type="text" value={systemType} onChange={e => setSystemType(e.target.value)} required />
          <button type="submit" className="bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded">Discover</button>
        </form>
        {message && <p className="mt-2 text-sm text-blue-600">{message}</p>}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {processes.map(p => (
          <div key={p.id} className="p-4 bg-white shadow rounded border-l-4 border-blue-500">
            <h3 className="font-bold text-lg">{p.name}</h3>
            <p className="text-gray-600 text-sm mb-2">{p.description}</p>
            <p className="text-sm">System: <span className="font-semibold">{p.systemType}</span></p>
            <Link href={`/processes/${p.id}`} className="mt-4 inline-block text-blue-500 hover:underline">View Details & Analysis &rarr;</Link>
          </div>
        ))}
        {processes.length === 0 && <p>No processes discovered yet.</p>}
      </div>
    </div>
  );
}
