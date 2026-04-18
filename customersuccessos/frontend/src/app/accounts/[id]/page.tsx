"use client";

import { useEffect, useState } from 'react';
import api from '../../../lib/api';

interface Account {
  id: string;
  name: string;
  subscriptionTier: string;
}

interface QbrDeck {
  id: string;
  content: string;
  status: string;
}

export default function AccountDetailsPage({ params }: { params: { id: string } }) {
  const [account, setAccount] = useState<Account | null>(null);
  const [qbrs, setQbrs] = useState<QbrDeck[]>([]);
  const [generating, setGenerating] = useState(false);

  useEffect(() => {
    api.get(`/api/accounts/${params.id}`).then(res => setAccount(res.data)).catch(console.error);
    loadQbrs();
  }, [params.id]);

  function loadQbrs() {
    api.get(`/api/accounts/${params.id}/qbr`).then(res => setQbrs(res.data)).catch(console.error);
  }

  async function generateQbr() {
    setGenerating(true);
    try {
      await api.post(`/api/accounts/${params.id}/qbr`);
      loadQbrs();
    } catch (e) {
      console.error(e);
    } finally {
      setGenerating(false);
    }
  }

  if (!account) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">{account.name} Details</h1>
      <p>Tier: {account.subscriptionTier}</p>
      
      <div className="mt-8">
        <h2 className="text-xl font-bold mb-2">QBR Decks</h2>
        <button 
          onClick={generateQbr} 
          disabled={generating}
          className="bg-blue-500 text-white px-4 py-2 rounded disabled:opacity-50 mb-4"
        >
          {generating ? 'Generating...' : 'Generate QBR'}
        </button>

        <div className="space-y-4">
          {qbrs.map(qbr => (
            <div key={qbr.id} className="border p-4 rounded">
              <p className="font-bold">Status: {qbr.status}</p>
              <pre className="mt-2 whitespace-pre-wrap">{qbr.content}</pre>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
