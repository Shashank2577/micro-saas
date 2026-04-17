'use client';

import { useState } from 'react';
import { api } from '@/lib/api';

export default function RecorderPage() {
  const [isRecording, setIsRecording] = useState(false);
  const [transcript, setTranscript] = useState('');
  const [encounterId, setEncounterId] = useState('');

  const handleRecord = async () => {
    setIsRecording(true);
    // Simulate recording for 2 seconds
    setTimeout(async () => {
      setIsRecording(false);
      try {
        const encounter = await api.transcribe('pat-123', 'clin-456');
        setTranscript(encounter.transcript);
        setEncounterId(encounter.id);
      } catch (err) {
        console.error(err);
      }
    }, 2000);
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Voice Recorder</h1>
      <button
        onClick={handleRecord}
        className={`px-4 py-2 rounded text-white ${isRecording ? 'bg-red-500 animate-pulse' : 'bg-blue-600'}`}
      >
        {isRecording ? 'Recording...' : 'Start Recording'}
      </button>

      {transcript && (
        <div className="mt-8 p-4 bg-white border rounded">
          <h2 className="font-bold mb-2">Transcript</h2>
          <p className="text-gray-700">{transcript}</p>
          <p className="text-sm text-gray-400 mt-2">Encounter ID: {encounterId}</p>
        </div>
      )}
    </div>
  );
}
