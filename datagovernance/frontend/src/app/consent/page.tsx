'use client';
import { useState } from 'react';
import { api, ConsentRecord } from '@/lib/api';

export default function ConsentPage() {
  const [records, setRecords] = useState<ConsentRecord[]>([]);
  const [email, setEmail] = useState('');
  const [searched, setSearched] = useState(false);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) return;
    try {
      const data = await api.consent.list(email);
      setRecords(data);
      setSearched(true);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Consent Management</h1>
      
      <form onSubmit={handleSearch} className="bg-white p-6 rounded-lg shadow-sm border mb-8 flex gap-4 items-end">
        <div className="flex-1">
          <label className="block text-sm font-medium mb-1">Search User Email</label>
          <input required type="email" value={email} onChange={e => setEmail(e.target.value)} className="w-full border rounded p-2" placeholder="user@example.com" />
        </div>
        <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700">Search</button>
      </form>

      {searched && (
        <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-50 border-b">
              <tr>
                <th className="p-4 text-left">Purpose</th>
                <th className="p-4 text-left">Granted</th>
                <th className="p-4 text-left">Timestamp</th>
              </tr>
            </thead>
            <tbody>
              {records.map(r => (
                <tr key={r.id} className="border-b">
                  <td className="p-4">{r.processingPurpose}</td>
                  <td className="p-4">
                    {r.isGranted ? 
                      <span className="text-green-600 font-medium">Yes</span> : 
                      <span className="text-red-600 font-medium">No</span>
                    }
                  </td>
                  <td className="p-4 text-gray-500">{new Date(r.timestamp).toLocaleString()}</td>
                </tr>
              ))}
              {records.length === 0 && (
                <tr><td colSpan={3} className="p-4 text-center text-gray-500">No consent records found for this user.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
