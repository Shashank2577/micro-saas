"use client";
import React, { useEffect, useState } from 'react';
import { fetchContext } from '../lib/api';

export function ContextTreeViewer({ customerId }: { customerId: string }) {
  const [context, setContext] = useState<any>(null);

  useEffect(() => {
    fetchContext(customerId)
      .then(setContext)
      .catch(console.error);
  }, [customerId]);

  return (
    <div className="bg-white p-4 shadow rounded-lg border border-gray-200 mt-4">
      <h2 className="text-xl font-semibold mb-2">Raw Context (JSON)</h2>
      <div className="bg-gray-100 p-4 rounded overflow-auto h-64 text-sm font-mono">
        {context ? JSON.stringify(context, null, 2) : 'Loading context...'}
      </div>
    </div>
  );
}
