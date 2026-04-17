'use client';
import { useState, useEffect } from 'react';
import { api, DataSubjectRequest } from '@/lib/api';

export default function DsarPage() {
  const [requests, setRequests] = useState<DataSubjectRequest[]>([]);
  const [email, setEmail] = useState('');
  const [type, setType] = useState('ACCESS');

  const fetchRequests = async () => {
    try {
      const data = await api.dsar.list();
      setRequests(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchRequests();
  }, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.dsar.create({ subjectEmail: email, requestType: type });
      setEmail('');
      fetchRequests();
    } catch (error) {
      console.error(error);
    }
  };

  const handleProcess = async (id: string) => {
    try {
      await api.dsar.process(id);
      fetchRequests();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">DSAR Management</h1>
      
      <form onSubmit={handleCreate} className="bg-white p-6 rounded-lg shadow-sm border mb-8 flex gap-4 items-end">
        <div>
          <label className="block text-sm font-medium mb-1">Subject Email</label>
          <input required type="email" value={email} onChange={e => setEmail(e.target.value)} className="border rounded p-2" />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Request Type</label>
          <select value={type} onChange={e => setType(e.target.value)} className="border rounded p-2 bg-white">
            <option value="ACCESS">Access</option>
            <option value="DELETION">Deletion</option>
          </select>
        </div>
        <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">Submit Request</button>
      </form>

      <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4 text-left">Email</th>
              <th className="p-4 text-left">Type</th>
              <th className="p-4 text-left">Status</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {requests.map(r => (
              <tr key={r.id} className="border-b">
                <td className="p-4">{r.subjectEmail}</td>
                <td className="p-4">{r.requestType}</td>
                <td className="p-4">
                  <span className={`px-2 py-1 rounded text-xs ${r.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'}`}>
                    {r.status}
                  </span>
                </td>
                <td className="p-4 text-right">
                  {r.status !== 'COMPLETED' && (
                    <button onClick={() => handleProcess(r.id)} className="text-blue-600 hover:text-blue-800">Process</button>
                  )}
                </td>
              </tr>
            ))}
             {requests.length === 0 && (
              <tr><td colSpan={4} className="p-4 text-center text-gray-500">No requests found.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
