"use client";
import React, { useEffect, useState } from 'react';
import { fetchVersions } from '../lib/api';

export function ContextDashboard({ customerId }: { customerId: string }) {
  const [data, setData] = useState<any>(null);

  useEffect(() => {
    fetchVersions(customerId)
      .then(setData)
      .catch(console.error);
  }, [customerId]);

  return (
    <div className="bg-white p-4 shadow rounded-lg border border-gray-200">
      <h2 className="text-xl font-semibold mb-2">Context Versions for {customerId}</h2>
      {data ? (
        <ul className="divide-y divide-gray-200">
          {data.map((version: any) => (
            <li key={version.versionId} className="py-2">
              <span className="font-medium">{new Date(version.createdAt).toLocaleString()}</span>
              <p className="text-sm text-gray-500">App: {version.createdByApp}</p>
              <p className="text-sm">Action: {version.changeDescription}</p>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-gray-500">Loading context data...</p>
      )}
    </div>
  );
}
