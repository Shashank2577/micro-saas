import React from 'react';
import { CachePolicy } from '../api/api';

export const PolicyList = ({ policies }: { policies: CachePolicy[] }) => {
  return (
    <div className="overflow-x-auto shadow-sm ring-1 ring-black ring-opacity-5 rounded-lg">
      <table className="min-w-full divide-y divide-gray-300">
        <thead className="bg-gray-50">
          <tr>
            <th className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900">App</th>
            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Namespace</th>
            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Strategy</th>
            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">TTL (s)</th>
            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Compression</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-200 bg-white">
          {policies.map((p) => (
            <tr key={p.id}>
              <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900">{p.appName}</td>
              <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{p.namespace}</td>
              <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{p.strategy}</td>
              <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{p.ttlSeconds}</td>
              <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{p.compressionEnabled ? 'Yes' : 'No'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
