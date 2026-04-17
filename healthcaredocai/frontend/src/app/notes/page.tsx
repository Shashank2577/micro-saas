'use client';

import { useState } from 'react';
import { api } from '@/lib/api';

export default function NotesPage() {
  const [encounterId, setEncounterId] = useState('');
  const [noteContent, setNoteContent] = useState('');
  const [loading, setLoading] = useState(false);

  const handleGenerate = async () => {
    if (!encounterId) return;
    setLoading(true);
    try {
      const note = await api.generateNote(encounterId, 'SOAP', 'General Practice');
      setNoteContent(note.content);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Note Editor</h1>
      <div className="flex gap-4 mb-4">
        <input
          type="text"
          value={encounterId}
          onChange={(e) => setEncounterId(e.target.value)}
          placeholder="Enter Encounter ID"
          className="border p-2 flex-grow rounded"
        />
        <button
          onClick={handleGenerate}
          disabled={loading || !encounterId}
          className="px-4 py-2 bg-blue-600 text-white rounded disabled:bg-gray-400"
        >
          {loading ? 'Generating...' : 'Generate SOAP Note'}
        </button>
      </div>

      {noteContent && (
        <div className="mt-4">
          <textarea
            value={noteContent}
            onChange={(e) => setNoteContent(e.target.value)}
            className="w-full h-96 border p-4 rounded font-mono"
          />
        </div>
      )}
    </div>
  );
}
