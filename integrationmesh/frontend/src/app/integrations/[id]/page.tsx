"use client";

import { useEffect, useState } from 'react';
import { api, Integration, FieldMapping, SyncHistory } from '@/lib/api';

export default function IntegrationDetails({ params }: { params: { id: string } }) {
  const [integration, setIntegration] = useState<Integration | null>(null);
  const [mappings, setMappings] = useState<FieldMapping[]>([]);
  const [history, setHistory] = useState<SyncHistory[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadData() {
      try {
        const [intData, mapData, histData] = await Promise.all([
          api.integrations.list().then(list => list.find(i => i.id === params.id) || null),
          api.mappings.list(params.id),
          api.history.list(params.id)
        ]);
        setIntegration(intData);
        setMappings(mapData);
        setHistory(histData);
      } catch (error) {
        console.error('Error fetching integration details', error);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, [params.id]);

  const suggestMappings = async () => {
    try {
      const suggested = await api.mappings.suggest(params.id);
      setMappings(prev => [...prev, ...suggested]);
    } catch (error) {
      console.error('Error suggesting mappings', error);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (!integration) return <div>Integration not found</div>;

  return (
    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8 space-y-6">
      <div className="bg-white shadow px-4 py-5 sm:rounded-lg sm:p-6">
        <h2 className="text-xl font-bold">{integration.name}</h2>
        <p className="text-gray-500">Status: {integration.status}</p>
      </div>

      <div className="bg-white shadow sm:rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg font-medium text-gray-900">Field Mappings</h3>
            <button onClick={suggestMappings} className="bg-blue-600 text-white px-4 py-2 rounded">
              Suggest AI Mappings
            </button>
          </div>
          <ul className="divide-y divide-gray-200">
            {mappings.map(map => (
              <li key={map.id} className="py-4 flex justify-between">
                <span>{map.sourceField} ➔ {map.targetField}</span>
                {map.isAiSuggested && (
                  <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                    AI Suggested ({map.confidenceScore})
                  </span>
                )}
              </li>
            ))}
          </ul>
        </div>
      </div>

      <div className="bg-white shadow sm:rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Sync History</h3>
          <ul className="divide-y divide-gray-200">
            {history.map(hist => (
              <li key={hist.id} className="py-4">
                <div className="flex justify-between">
                  <span className="font-medium text-gray-900">{new Date(hist.startedAt).toLocaleString()}</span>
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${hist.status === 'SUCCESS' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                    {hist.status}
                  </span>
                </div>
                <p className="text-sm text-gray-500 mt-1">Processed: {hist.recordsProcessed} | Failed: {hist.recordsFailed}</p>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
