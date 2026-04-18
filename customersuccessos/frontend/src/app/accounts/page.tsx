"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import api from '../../lib/api';

interface Account {
  id: string;
  name: string;
  subscriptionTier: string;
  csmName: string;
}

export default function AccountsPage() {
  const [accounts, setAccounts] = useState<Account[]>([]);

  useEffect(() => {
    api.get('/api/accounts').then(res => setAccounts(res.data)).catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Accounts</h1>
      <table className="w-full border-collapse border">
        <thead>
          <tr className="bg-gray-100">
            <th className="border p-2">Name</th>
            <th className="border p-2">Tier</th>
            <th className="border p-2">CSM</th>
            <th className="border p-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {accounts.map(acc => (
            <tr key={acc.id}>
              <td className="border p-2">{acc.name}</td>
              <td className="border p-2">{acc.subscriptionTier}</td>
              <td className="border p-2">{acc.csmName}</td>
              <td className="border p-2">
                <Link href={`/accounts/${acc.id}`} className="text-blue-500 hover:underline">View Details</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
