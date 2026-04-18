"use client";

import { useEffect, useState } from 'react';
import api from '../lib/api';

export default function IntentBuilder({ agentId }: { agentId: string }) {
  const [intents, setIntents] = useState([]);
  const [form, setForm] = useState({ name: '', description: '', actionType: 'ESCALATE', actionConfig: '' });

  const loadIntents = async () => {
    try {
      const res = await api.get(`/api/v1/agents/${agentId}/intents`);
      setIntents(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => { loadIntents(); }, [agentId]);

  const handleAdd = async () => {
    if (!form.name) return;
    try {
      await api.post(`/api/v1/agents/${agentId}/intents`, form);
      setForm({ name: '', description: '', actionType: 'ESCALATE', actionConfig: '' });
      loadIntents();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await api.delete(`/api/v1/agents/${agentId}/intents/${id}`);
      loadIntents();
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div>
      <h3 className="text-2xl font-bold mb-4">Intents & Actions</h3>
      <div className="bg-white p-6 rounded shadow mb-6 space-y-4">
        <h4 className="font-semibold text-lg">Add New Intent</h4>
        <input className="w-full border p-2 rounded" placeholder="Intent Name (e.g. Talk to Human)" value={form.name} onChange={e => setForm({...form, name: e.target.value})} />
        <input className="w-full border p-2 rounded" placeholder="Description (When to trigger)" value={form.description} onChange={e => setForm({...form, description: e.target.value})} />
        <select className="w-full border p-2 rounded" value={form.actionType} onChange={e => setForm({...form, actionType: e.target.value})}>
          <option value="ESCALATE">Escalate to Human</option>
          <option value="END_CALL">End Call</option>
          <option value="API_CALL">Trigger API Webhook</option>
        </select>
        <button onClick={handleAdd} className="bg-blue-600 text-white px-4 py-2 rounded">Create Intent</button>
      </div>

      <div className="space-y-4">
        {intents.map((intent: any) => (
          <div key={intent.id} className="bg-white p-4 rounded shadow flex justify-between items-start border-l-4 border-blue-600">
            <div>
              <h5 className="font-bold text-lg">{intent.name}</h5>
              <p className="text-gray-600">{intent.description}</p>
              <span className="inline-block mt-2 bg-gray-200 rounded px-2 py-1 text-xs font-semibold text-gray-700">Action: {intent.actionType}</span>
            </div>
            <button onClick={() => handleDelete(intent.id)} className="text-red-600 hover:underline">Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
}
