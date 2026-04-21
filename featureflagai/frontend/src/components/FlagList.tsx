"use client";

import React, { useState, useEffect } from 'react';
import { api } from '@/lib/api';

type Flag = {
  id: string;
  name: string;
  enabled: boolean;
  rolloutPct: number;
};

export default function FlagList() {
  const [flags, setFlags] = useState<Flag[]>([]);

  useEffect(() => {
    fetch('/api/flags')
      .then(res => res.json())
      .then(data => setFlags(data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="bg-white p-4 shadow rounded" data-testid="flag-list">
      {flags.length === 0 ? (
        <p>No flags found.</p>
      ) : (
        <ul className="space-y-3">
          {flags.map((f) => (
            <li key={f.id} className="border p-3 rounded flex justify-between items-center">
              <div>
                <strong>{f.name}</strong>
                <p className="text-sm text-gray-600">Rollout: {f.rolloutPct}%</p>
              </div>
              <div>
                <span className={`px-2 py-1 rounded text-xs text-white ${f.enabled ? 'bg-green-500' : 'bg-red-500'}`}>
                  {f.enabled ? 'Enabled' : 'Disabled'}
                </span>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
