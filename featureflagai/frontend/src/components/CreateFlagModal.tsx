import { useState } from 'react';
import api from '../lib/api';

export default function CreateFlagModal({ onClose, onCreated }: { onClose: () => void, onCreated: () => void }) {
  const [name, setName] = useState('');
  const [enabled, setEnabled] = useState(false);
  const [rolloutPct, setRolloutPct] = useState(0);

  const submit = async (e: any) => {
    e.preventDefault();
    await api.post('/flags', { name, enabled, rolloutPct, rules: {} });
    onCreated();
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center">
      <div className="bg-white p-6 rounded shadow-lg w-96">
        <h2 className="text-xl font-bold mb-4">Create New Flag</h2>
        <form onSubmit={submit}>
          <label className="block mb-2">
            Name:
            <input type="text" value={name} onChange={e => setName(e.target.value)} required className="border p-2 w-full mt-1"/>
          </label>
          <label className="block mb-2">
            Enabled:
            <input type="checkbox" checked={enabled} onChange={e => setEnabled(e.target.checked)} className="ml-2"/>
          </label>
          <label className="block mb-4">
            Rollout %:
            <input type="number" value={rolloutPct} onChange={e => setRolloutPct(Number(e.target.value))} required className="border p-2 w-full mt-1" min="0" max="100"/>
          </label>
          <div className="flex justify-end gap-2">
            <button type="button" onClick={onClose} className="px-4 py-2 border rounded">Cancel</button>
            <button type="submit" className="px-4 py-2 bg-blue-500 text-white rounded">Create</button>
          </div>
        </form>
      </div>
    </div>
  );
}
