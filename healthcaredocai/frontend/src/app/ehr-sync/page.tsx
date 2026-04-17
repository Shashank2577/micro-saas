'use client';

import { useState } from 'react';
import { api } from '@/lib/api';

export default function EHRSyncPage() {
  const [noteId, setNoteId] = useState('');
  const [ehrSystem, setEhrSystem] = useState('EPIC');
  const [status, setStatus] = useState('');

  const handleSync = async () => {
    if (!noteId) return;
    setStatus('Syncing...');
    try {
      const result = await api.syncToEHR(noteId, ehrSystem);
      setStatus(result);
    } catch (err) {
      setStatus('Failed to sync. Ensure the note is approved.');
      console.error(err);
    }
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">EHR Sync Dashboard</h1>
      <div className="flex flex-col gap-4">
        <input
          type="text"
          value={noteId}
          onChange={(e) => setNoteId(e.target.value)}
          placeholder="Enter Approved Note ID"
          className="border p-2 rounded"
        />
        <select
          value={ehrSystem}
          onChange={(e) => setEhrSystem(e.target.value)}
          className="border p-2 rounded"
        >
          <option value="EPIC">Epic</option>
          <option value="CERNER">Cerner</option>
        </select>
        <button
          onClick={handleSync}
          className="px-4 py-2 bg-green-600 text-white rounded"
        >
          Sync to EHR
        </button>
      </div>
      {status && (
        <div className="mt-4 p-4 border rounded bg-gray-50">
          {status}
        </div>
      )}
    </div>
  );
}
