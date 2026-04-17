'use client';
import { useState } from 'react';
import { api, DataLineageNode } from '@/lib/api';

export default function LineagePage() {
  const [nodes, setNodes] = useState<DataLineageNode[]>([]);
  const [field, setField] = useState('');
  const [searched, setSearched] = useState(false);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!field) return;
    try {
      const data = await api.lineage.get(field);
      setNodes(data);
      setSearched(true);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Data Lineage</h1>
      
      <form onSubmit={handleSearch} className="bg-white p-6 rounded-lg shadow-sm border mb-8 flex gap-4 items-end">
        <div className="flex-1">
          <label className="block text-sm font-medium mb-1">Field Name to Trace</label>
          <input required type="text" value={field} onChange={e => setField(e.target.value)} className="w-full border rounded p-2" placeholder="e.g. customer_revenue" />
        </div>
        <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700">Trace</button>
      </form>

      {searched && (
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <h2 className="text-xl font-semibold mb-4">Lineage Trail for "{field}"</h2>
          {nodes.length > 0 ? (
            <div className="space-y-4">
              {nodes.map((node, i) => (
                <div key={node.id} className="flex items-center gap-4">
                  <div className="w-8 h-8 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center font-bold">
                    {i + 1}
                  </div>
                  <div className="flex-1 border p-4 rounded bg-gray-50">
                    <div className="font-medium text-gray-900">{node.originService} &rarr; {node.currentService}</div>
                    <div className="text-sm text-gray-500 mt-1">Logic: {node.transformationLogic}</div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-gray-500">No lineage data found for this field.</p>
          )}
        </div>
      )}
    </div>
  );
}
