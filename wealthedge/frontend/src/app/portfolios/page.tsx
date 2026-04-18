"use client";

import React, { useEffect, useState } from 'react';
import api from '../../lib/api';

interface Portfolio {
  id: string;
  name: string;
  totalValue: number;
}

export default function PortfoliosPage() {
  const [portfolios, setPortfolios] = useState<Portfolio[]>([]);
  const [name, setName] = useState('');

  useEffect(() => {
    fetchPortfolios();
  }, []);

  const fetchPortfolios = () => {
    api.get('/api/portfolios').then(res => setPortfolios(res.data)).catch(console.error);
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    await api.post('/api/portfolios', { name });
    fetchPortfolios();
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Portfolios</h1>
      
      <form onSubmit={handleCreate} className="mb-8 flex gap-4">
        <input className="border p-2" placeholder="Portfolio Name" value={name} onChange={e => setName(e.target.value)} required />
        <button className="bg-purple-500 text-white px-4 py-2 rounded" type="submit">Create Portfolio</button>
      </form>

      <ul className="grid grid-cols-2 gap-4">
        {portfolios.map(p => (
          <li key={p.id} className="border p-4 rounded shadow">
            <h3 className="font-semibold text-lg">{p.name}</h3>
            <p className="text-gray-600">Total Value: ${p.totalValue.toLocaleString()}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}
