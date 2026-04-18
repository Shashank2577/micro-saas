import React, { useState } from 'react';

export default function ConsolidationOfferForm({ onSubmit }: { onSubmit: (data: any) => void }) {
  const [name, setName] = useState('');
  const [status, setStatus] = useState('ACTIVE');

  return (
    <form onSubmit={(e) => { e.preventDefault(); onSubmit({ name, status }); }} className="space-y-4">
      <div>
        <label className="block">Name</label>
        <input value={name} onChange={e => setName(e.target.value)} className="border p-2 w-full" />
      </div>
      <div>
        <label className="block">Status</label>
        <input value={status} onChange={e => setStatus(e.target.value)} className="border p-2 w-full" />
      </div>
      <button type="submit" className="bg-blue-500 text-white p-2 rounded">Submit</button>
    </form>
  );
}
