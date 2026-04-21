import { useState } from 'react';
import api from '../lib/api';

export default function SegmentList({ flagId, initialSegments }: any) {
  const [segments, setSegments] = useState<any[]>(initialSegments || []);
  const [newSegmentName, setNewSegmentName] = useState('');

  const addSegment = async () => {
    const res = await api.post(`/flags/${flagId}/segments`, {
      name: newSegmentName,
      conditions: { test: true }
    });
    setSegments([...segments, res.data]);
    setNewSegmentName('');
  };

  return (
    <div className="border p-4 bg-gray-50 rounded">
      <h2 className="text-xl font-bold mb-4">Targeting Segments</h2>
      <ul className="mb-4">
        {segments.map(s => (
          <li key={s.id} className="mb-2 p-2 bg-white border rounded">
            <strong>{s.name}</strong> - <span className="text-gray-500 text-sm">{JSON.stringify(s.conditions)}</span>
          </li>
        ))}
      </ul>
      <div className="flex gap-2">
        <input type="text" value={newSegmentName} onChange={e => setNewSegmentName(e.target.value)} placeholder="New segment name" className="border p-2 flex-grow"/>
        <button onClick={addSegment} className="bg-green-500 text-white px-4 py-2 rounded">Add Segment</button>
      </div>
    </div>
  );
}
