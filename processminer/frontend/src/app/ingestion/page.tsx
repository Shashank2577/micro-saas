"use client";

import { useState } from 'react';
import api from '@/lib/api';
import Link from 'next/link';

export default function IngestionPage() {
  const [systemType, setSystemType] = useState('');
  const [caseId, setCaseId] = useState('');
  const [activityName, setActivityName] = useState('');
  const [message, setMessage] = useState('');

  const handleIngest = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/api/events/ingest', {
        systemType,
        caseId,
        activityName,
        timestamp: new Date().toISOString()
      });
      setMessage('Event ingested successfully');
      setSystemType('');
      setCaseId('');
      setActivityName('');
    } catch (error) {
      setMessage('Error ingesting event');
    }
  };

  return (
    <div className="container mx-auto p-4">
      <Link href="/" className="text-blue-500 mb-4 inline-block">&larr; Back to Dashboard</Link>
      <h1 className="text-2xl font-bold mb-4">Ingest Event Log</h1>
      <form onSubmit={handleIngest} className="bg-white p-6 shadow rounded max-w-md">
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">System Type</label>
          <input className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700" type="text" value={systemType} onChange={e => setSystemType(e.target.value)} required />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">Case ID</label>
          <input className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700" type="text" value={caseId} onChange={e => setCaseId(e.target.value)} required />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">Activity Name</label>
          <input className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700" type="text" value={activityName} onChange={e => setActivityName(e.target.value)} required />
        </div>
        <button type="submit" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Ingest</button>
        {message && <p className="mt-4 text-green-600 font-bold">{message}</p>}
      </form>
    </div>
  );
}
