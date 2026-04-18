"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import api from '../../../lib/api';

export default function NewAgentPage() {
  const router = useRouter();
  const [form, setForm] = useState({
    name: '',
    description: '',
    systemPrompt: 'You are a helpful voice assistant.',
    voiceId: '',
    language: 'en-US'
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await api.post('/api/v1/agents', form);
      router.push(`/agents/${res.data.id}`);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Create New Agent</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium">Name</label>
          <input required type="text" className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" 
            value={form.name} onChange={e => setForm({...form, name: e.target.value})} />
        </div>
        <div>
          <label className="block text-sm font-medium">Description</label>
          <textarea className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" 
            value={form.description} onChange={e => setForm({...form, description: e.target.value})} />
        </div>
        <div>
          <label className="block text-sm font-medium">System Prompt</label>
          <textarea required rows={5} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" 
            value={form.systemPrompt} onChange={e => setForm({...form, systemPrompt: e.target.value})} />
        </div>
        <div>
          <label className="block text-sm font-medium">Voice ID</label>
          <input type="text" className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" 
            value={form.voiceId} onChange={e => setForm({...form, voiceId: e.target.value})} />
        </div>
        <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded">Create Agent</button>
      </form>
    </div>
  );
}
