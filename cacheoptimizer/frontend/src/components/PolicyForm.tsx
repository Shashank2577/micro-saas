import React, { useState } from 'react';
import { api, CachePolicy } from '../api/api';

export const PolicyForm = ({ onSuccess }: { onSuccess: () => void }) => {
  const [formData, setFormData] = useState<Partial<CachePolicy>>({
    appName: '',
    namespace: '',
    ttlSeconds: 3600,
    strategy: 'CACHE_ASIDE',
    compressionEnabled: false,
    staleWhileRevalidate: false,
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.createPolicy(formData);
      onSuccess();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-md">
      <div>
        <label className="block text-sm font-medium">App Name</label>
        <input 
          className="mt-1 block w-full border rounded-md p-2 text-black"
          value={formData.appName} 
          onChange={e => setFormData({...formData, appName: e.target.value})} 
          required 
        />
      </div>
      <div>
        <label className="block text-sm font-medium">Namespace</label>
        <input 
          className="mt-1 block w-full border rounded-md p-2 text-black"
          value={formData.namespace} 
          onChange={e => setFormData({...formData, namespace: e.target.value})} 
          required 
        />
      </div>
      <div>
        <label className="block text-sm font-medium">TTL Seconds</label>
        <input 
          type="number"
          className="mt-1 block w-full border rounded-md p-2 text-black"
          value={formData.ttlSeconds} 
          onChange={e => setFormData({...formData, ttlSeconds: parseInt(e.target.value)})} 
          required 
        />
      </div>
      <div>
        <label className="block text-sm font-medium">Strategy</label>
        <select 
          className="mt-1 block w-full border rounded-md p-2 text-black"
          value={formData.strategy}
          onChange={e => setFormData({...formData, strategy: e.target.value as any})}
        >
          <option value="CACHE_ASIDE">CACHE_ASIDE</option>
          <option value="WRITE_THROUGH">WRITE_THROUGH</option>
          <option value="WRITE_BEHIND">WRITE_BEHIND</option>
        </select>
      </div>
      <div>
        <label className="flex items-center space-x-2">
          <input 
            type="checkbox"
            checked={formData.compressionEnabled}
            onChange={e => setFormData({...formData, compressionEnabled: e.target.checked})}
          />
          <span className="text-sm font-medium">Enable Compression</span>
        </label>
      </div>
      <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
        Create Policy
      </button>
    </form>
  );
};
