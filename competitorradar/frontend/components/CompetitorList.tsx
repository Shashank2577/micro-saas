"use client";
import React, { useEffect, useState } from 'react';
import { fetchCompetitors, addCompetitor, deleteCompetitor } from '../lib/api';

export default function CompetitorList() {
  const [competitors, setCompetitors] = useState<any[]>([]);
  const [name, setName] = useState('');

  useEffect(() => {
    loadCompetitors();
  }, []);

  const loadCompetitors = async () => {
    try {
      const data = await fetchCompetitors();
      setCompetitors(data);
    } catch (e) {
      console.error(e);
    }
  };

  const handleAdd = async () => {
    if (!name) return;
    try {
      await addCompetitor({ name });
      setName('');
      loadCompetitors();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await deleteCompetitor(id);
      loadCompetitors();
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="p-4 border rounded shadow">
      <h2 className="text-xl font-bold mb-4">Competitors</h2>
      <ul className="mb-4">
        {competitors.map(c => (
          <li key={c.id} className="flex justify-between items-center mb-2">
            <span>{c.name}</span>
            <button
              data-testid={`delete-${c.name}`}
              onClick={() => handleDelete(c.id)}
              className="text-red-500 hover:text-red-700"
            >
              Remove
            </button>
          </li>
        ))}
      </ul>
      <div className="flex gap-2">
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="New Competitor Name"
          className="border p-2 rounded"
        />
        <button onClick={handleAdd} className="bg-blue-500 text-white p-2 rounded">
          Add Competitor
        </button>
      </div>
    </div>
  );
}
