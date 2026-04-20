'use client';
import { useEffect, useState } from 'react';
import api from '@/lib/api';

interface VoiceSelectorProps {
  projectId: string;
  transcriptionId: string;
}

export default function VoiceSelector({ projectId, transcriptionId }: VoiceSelectorProps) {
  const [voices, setVoices] = useState<any[]>([]);
  const [selectedVoice, setSelectedVoice] = useState('');
  const [narrating, setNarrating] = useState(false);

  useEffect(() => {
    api.get('/voices').then(res => {
      setVoices(res.data);
      if (res.data.length > 0) {
        setSelectedVoice(res.data[0].id);
      }
    });
  }, []);

  const handleNarrate = async () => {
    const voice = voices.find(v => v.id === selectedVoice);
    if (!voice) return;

    setNarrating(true);
    try {
      await api.post(`/projects/${projectId}/narrate`, {
        voiceProvider: voice.provider,
        voiceId: voice.id,
        transcriptionId
      });
      alert('Narration generation started');
    } catch (err) {
      console.error(err);
      alert('Failed to start narration');
    } finally {
      setNarrating(false);
    }
  };

  return (
    <div className="flex items-center gap-2">
      <select
        value={selectedVoice}
        onChange={e => setSelectedVoice(e.target.value)}
        className="border p-2 rounded"
      >
        {voices.map(v => (
          <option key={v.id} value={v.id}>{v.name}</option>
        ))}
      </select>
      <button
        onClick={handleNarrate}
        disabled={narrating}
        className="bg-green-600 text-white px-4 py-2 rounded disabled:bg-gray-400"
      >
        {narrating ? 'Generating...' : 'Generate Narration'}
      </button>
    </div>
  );
}
