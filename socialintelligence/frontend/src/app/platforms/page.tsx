'use client';
import { useEffect, useState } from 'react';

export default function Platforms() {
  const [accounts, setAccounts] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/accounts', { headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => setAccounts(data))
      .catch(console.error);
  }, []);

  const connectPlatform = (platform: string) => {
    fetch(`/api/accounts/connect/${platform}`, { method: 'POST', headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => {
        if (data.authorizationUrl) window.location.href = data.authorizationUrl;
      })
      .catch(console.error);
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Connected Platforms</h1>
      <div className="grid grid-cols-2 gap-4 mb-8">
        {['INSTAGRAM', 'TIKTOK', 'YOUTUBE', 'TWITTER', 'LINKEDIN'].map(p => (
          <button key={p} onClick={() => connectPlatform(p)} className="bg-blue-500 text-white p-4 rounded hover:bg-blue-600">
            Connect {p}
          </button>
        ))}
      </div>
      <h2 className="text-xl font-semibold mb-4">Active Accounts</h2>
      <ul>
        {accounts.map(acc => (
          <li key={acc.id} className="bg-white p-4 mb-2 rounded shadow flex justify-between">
            <span>{acc.platformName} - {acc.accountName}</span>
            <span className="text-green-500">Active</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
