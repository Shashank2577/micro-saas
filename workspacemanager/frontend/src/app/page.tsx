"use client";

import { useState, useEffect } from 'react';
import api from '../lib/api';

export default function WorkspaceSettings() {
  const [workspace, setWorkspace] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchWorkspace();
  }, []);

  const fetchWorkspace = async () => {
    try {
      const { data } = await api.get('/api/v1/workspaces');
      setWorkspace(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.put('/api/v1/workspaces', {
        name: workspace.name,
        subdomain: workspace.subdomain,
        brandingLogoUrl: workspace.brandingLogoUrl,
        features: workspace.features
      });
      alert('Workspace updated successfully');
    } catch (err) {
      console.error(err);
      alert('Failed to update workspace');
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold text-gray-900">Workspace Settings</h1>
      
      {!workspace ? (
        <div>No workspace found. Run backend with initialization.</div>
      ) : (
        <form onSubmit={handleUpdate} className="space-y-4 max-w-lg">
          <div>
            <label className="block text-sm font-medium text-gray-700">Workspace Name</label>
            <input
              type="text"
              value={workspace.name || ''}
              onChange={(e) => setWorkspace({...workspace, name: e.target.value})}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Subdomain</label>
            <input
              type="text"
              value={workspace.subdomain || ''}
              onChange={(e) => setWorkspace({...workspace, subdomain: e.target.value})}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
            />
          </div>
          <button
            type="submit"
            className="inline-flex justify-center rounded-md border border-transparent bg-indigo-600 py-2 px-4 text-sm font-medium text-white shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Save Settings
          </button>
        </form>
      )}
    </div>
  );
}
