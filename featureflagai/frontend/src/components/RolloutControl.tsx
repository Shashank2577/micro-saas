import { useState } from 'react';

export default function RolloutControl({ initialRollout, initialEnabled, onSave }: any) {
  const [rolloutPct, setRolloutPct] = useState(initialRollout);
  const [enabled, setEnabled] = useState(initialEnabled);

  return (
    <div className="border p-4 mb-6 bg-gray-50 rounded">
      <h2 className="text-xl font-bold mb-4">Rollout Control</h2>
      <label className="block mb-2">
        Enabled:
        <input type="checkbox" checked={enabled} onChange={e => setEnabled(e.target.checked)} className="ml-2"/>
      </label>
      <label className="block mb-4">
        Rollout %:
        <input type="number" value={rolloutPct} onChange={e => setRolloutPct(Number(e.target.value))} className="border p-1 ml-2 w-20" min="0" max="100"/>
      </label>
      <button onClick={() => onSave(rolloutPct, enabled)} className="bg-blue-500 text-white px-4 py-2 rounded">
        Save Changes
      </button>
    </div>
  );
}
