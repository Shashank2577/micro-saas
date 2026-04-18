"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';
import Link from 'next/link';

export default function CompliancePage() {
  const [policies, setPolicies] = useState<any[]>([]);
  const [models, setModels] = useState<any[]>([]);
  const [name, setName] = useState('');
  const [modelId, setModelId] = useState('');
  
  const fetchData = async () => {
    try {
      const [polRes, modRes] = await Promise.all([
        api.get('/api/policies'),
        api.get('/api/process-models')
      ]);
      setPolicies(polRes.data);
      setModels(modRes.data);
      if (modRes.data.length > 0) setModelId(modRes.data[0].id);
    } catch (e) { console.error(e); }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/api/policies', {
        name,
        processModelId: modelId,
        ruleDefinition: { strict: true }
      });
      setName('');
      fetchData();
    } catch (e) {
      alert('Error creating policy');
    }
  };

  return (
    <div className="container mx-auto p-4">
      <Link href="/" className="text-blue-500 mb-4 inline-block">&larr; Back to Dashboard</Link>
      <h1 className="text-2xl font-bold mb-6">Compliance Policies</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div>
          <h2 className="text-lg font-semibold mb-4">Create Policy</h2>
          <form onSubmit={handleCreate} className="bg-white p-4 shadow rounded">
            <div className="mb-4">
              <label className="block text-sm font-bold text-gray-700 mb-2">Policy Name</label>
              <input type="text" value={name} onChange={e=>setName(e.target.value)} required className="w-full border p-2 rounded" />
            </div>
            <div className="mb-4">
              <label className="block text-sm font-bold text-gray-700 mb-2">Apply to Model</label>
              <select value={modelId} onChange={e=>setModelId(e.target.value)} required className="w-full border p-2 rounded">
                {models.map(m => <option key={m.id} value={m.id}>{m.name}</option>)}
              </select>
            </div>
            <button type="submit" className="bg-blue-600 text-white font-bold py-2 px-4 rounded w-full">Save Policy</button>
          </form>
        </div>
        
        <div>
          <h2 className="text-lg font-semibold mb-4">Active Policies</h2>
          <div className="space-y-3">
            {policies.map(p => (
              <div key={p.id} className="bg-white p-4 shadow rounded border-l-4 border-indigo-500">
                <p className="font-bold">{p.name}</p>
                <p className="text-xs text-gray-500 mt-1">Model ID: {p.processModelId}</p>
              </div>
            ))}
            {policies.length === 0 && <p className="text-gray-500">No policies defined.</p>}
          </div>
        </div>
      </div>
    </div>
  );
}
