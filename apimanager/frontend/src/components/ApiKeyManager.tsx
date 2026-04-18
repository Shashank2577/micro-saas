'use client';

import React, { useState, useEffect } from 'react';
import { api } from '../lib/api';

interface ApiKey {
  id: string;
  prefix: string;
  status: string;
  createdAt: string;
}

export default function ApiKeyManager({ projectId }: { projectId: string }) {
  const [keys, setKeys] = useState<ApiKey[]>([]);
  const [newKey, setNewKey] = useState<string | null>(null);

  useEffect(() => {
    loadKeys();
  }, [projectId]);

  const loadKeys = async () => {
    try {
      const res = await api.get(`/api/v1/keys?projectId=${projectId}`);
      setKeys(res.data);
    } catch (e) {
      console.error('Failed to load keys', e);
    }
  };

  const generateKey = async () => {
    try {
      const res = await api.post('/api/v1/keys', { projectId });
      setNewKey(res.data.key);
      loadKeys();
    } catch (e) {
      console.error('Failed to generate key', e);
    }
  };

  const revokeKey = async (id: string) => {
    try {
      await api.delete(`/api/v1/keys/${id}`);
      loadKeys();
    } catch (e) {
      console.error('Failed to revoke key', e);
    }
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
      <h3 className="text-lg font-medium mb-4">API Keys</h3>
      
      <button 
        onClick={generateKey}
        className="mb-4 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
      >
        Generate New Key
      </button>

      {newKey && (
        <div className="mb-4 p-4 bg-green-50 border border-green-200 text-green-800 rounded-md">
          <p className="font-semibold">New API Key generated!</p>
          <p className="font-mono bg-white p-2 mt-2 rounded border">{newKey}</p>
          <p className="text-sm mt-2 text-green-600">Please copy this key now. You won't be able to see it again.</p>
        </div>
      )}

      <table className="min-w-full divide-y divide-gray-200">
        <thead>
          <tr>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Prefix</th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Created</th>
            <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-200">
          {keys.map((key) => (
            <tr key={key.id}>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-900">{key.prefix}...</td>
              <td className="px-6 py-4 whitespace-nowrap">
                <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${key.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                  {key.status}
                </span>
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {new Date(key.createdAt).toLocaleDateString()}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                {key.status === 'ACTIVE' && (
                  <button 
                    onClick={() => revokeKey(key.id)}
                    className="text-red-600 hover:text-red-900"
                  >
                    Revoke
                  </button>
                )}
              </td>
            </tr>
          ))}
          {keys.length === 0 && (
            <tr>
              <td colSpan={4} className="px-6 py-4 text-center text-gray-500">No API keys found.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
