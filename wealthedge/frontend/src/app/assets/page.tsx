"use client";

import React, { useEffect, useState } from 'react';
import api from '../../lib/api';

interface Asset {
  id: string;
  name: string;
  type: string;
  currentValue: number;
}

export default function AssetsPage() {
  const [assets, setAssets] = useState<Asset[]>([]);
  const [name, setName] = useState('');
  const [type, setType] = useState('TRADITIONAL');
  const [currentValue, setCurrentValue] = useState(0);

  useEffect(() => {
    fetchAssets();
  }, []);

  const fetchAssets = () => {
    api.get('/api/assets').then(res => setAssets(res.data)).catch(console.error);
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    await api.post('/api/assets', { name, type, currentValue });
    fetchAssets();
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Assets</h1>
      
      <form onSubmit={handleCreate} className="mb-8 flex gap-4">
        <input className="border p-2" placeholder="Name" value={name} onChange={e => setName(e.target.value)} required />
        <select className="border p-2" value={type} onChange={e => setType(e.target.value)}>
          <option value="TRADITIONAL">Traditional</option>
          <option value="REAL_ESTATE">Real Estate</option>
          <option value="ALTERNATIVE">Alternative</option>
          <option value="BUSINESS">Business</option>
        </select>
        <input className="border p-2" type="number" placeholder="Value" value={currentValue} onChange={e => setCurrentValue(Number(e.target.value))} required />
        <button className="bg-green-500 text-white px-4 py-2 rounded" type="submit">Add Asset</button>
      </form>

      <ul className="grid grid-cols-3 gap-4">
        {assets.map(asset => (
          <li key={asset.id} className="border p-4 rounded shadow">
            <h3 className="font-semibold">{asset.name}</h3>
            <p className="text-sm text-gray-500">{asset.type}</p>
            <p className="text-xl font-bold">${asset.currentValue.toLocaleString()}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}
