'use client';
import { useState } from 'react';
import { api } from '@/lib/api';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

export default function PoliciesPage() {
  const queryClient = useQueryClient();
  const [dataType, setDataType] = useState('');
  const [retentionDays, setRetentionDays] = useState(30);

  const { data: policies = [], isLoading } = useQuery({
    queryKey: ['policies'],
    queryFn: api.policies.list,
  });

  const createMutation = useMutation({
    mutationFn: api.policies.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['policies'] });
      setDataType('');
      setRetentionDays(30);
    },
  });

  const deleteMutation = useMutation({
    mutationFn: api.policies.delete,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['policies'] }),
  });

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    createMutation.mutate({ dataType, retentionDays });
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Retention Policies</h1>
      
      <form onSubmit={handleCreate} className="bg-white p-6 rounded-lg shadow-sm border mb-8 flex gap-4 items-end">
        <div>
          <label className="block text-sm font-medium mb-1">Data Type</label>
          <input required type="text" value={dataType} onChange={e => setDataType(e.target.value)} className="border rounded p-2" placeholder="e.g. USER_DATA" />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Retention Days</label>
          <input required type="number" value={retentionDays} onChange={e => setRetentionDays(Number(e.target.value))} className="border rounded p-2" />
        </div>
        <button type="submit" disabled={createMutation.isPending} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">Add Policy</button>
      </form>

      <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4 text-left">Data Type</th>
              <th className="p-4 text-left">Retention Days</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {isLoading && <tr><td colSpan={3} className="p-4 text-center">Loading...</td></tr>}
            {policies.map(p => (
              <tr key={p.id} className="border-b">
                <td className="p-4">{p.dataType}</td>
                <td className="p-4">{p.retentionDays}</td>
                <td className="p-4 text-right">
                  <button onClick={() => deleteMutation.mutate(p.id)} className="text-red-600 hover:text-red-800">Delete</button>
                </td>
              </tr>
            ))}
            {!isLoading && policies.length === 0 && (
              <tr><td colSpan={3} className="p-4 text-center text-gray-500">No policies found.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
