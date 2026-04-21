"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import api from '../lib/api';
import CreateFlagModal from '../components/CreateFlagModal';

export default function Home() {
  const [flags, setFlags] = useState<any[]>([]);
  const [showModal, setShowModal] = useState(false);

  const loadFlags = () => {
    api.get('/flags').then(res => setFlags(res.data)).catch(console.error);
  };

  useEffect(() => {
    loadFlags();
  }, []);

  return (
    <main className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Feature Flags</h1>
        <button onClick={() => setShowModal(true)} className="bg-blue-500 text-white px-4 py-2 rounded">Create Flag</button>
      </div>

      <div className="mb-4">
        <Link href="/impact" className="mr-4 text-blue-500 hover:underline">Impact Analysis</Link>
        <Link href="/cleanup" className="text-blue-500 hover:underline">Cleanup Dashboard</Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {flags.map(flag => (
          <div key={flag.id} className="border p-4 rounded shadow bg-white">
            <h2 className="text-xl font-semibold">{flag.name}</h2>
            <p className="text-gray-600">Enabled: {flag.enabled ? 'Yes' : 'No'}</p>
            <p className="text-gray-600">Rollout: {flag.rolloutPct}%</p>
            <Link href={`/flags/${flag.id}`} className="text-blue-500 hover:underline mt-2 inline-block">
              Manage Flag
            </Link>
          </div>
        ))}
        {flags.length === 0 && <p>No flags found.</p>}
      </div>

      {showModal && (
        <CreateFlagModal onClose={() => setShowModal(false)} onCreated={() => { setShowModal(false); loadFlags(); }} />
      )}
    </main>
  );
}
