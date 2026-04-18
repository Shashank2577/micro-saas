"use client";

import { useEffect, useState } from 'react';
import { fetchApi } from '@/lib/api';

interface CarrierPerformance {
  id: string;
  carrierName: string;
  onTimeRate: number;
  predictedDelayMins: number;
}

export default function Dashboard() {
  const [carriers, setCarriers] = useState<CarrierPerformance[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchApi('/api/carriers')
      .then((data) => setCarriers(data))
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-3xl font-bold mb-8 text-gray-900">Carrier Performance Dashboard</h1>
        
        {loading ? (
          <p>Loading carriers...</p>
        ) : (
          <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Carrier</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">On-Time Rate</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Predicted Delay</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {carriers.length > 0 ? carriers.map((carrier) => (
                  <tr key={carrier.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{carrier.carrierName}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{carrier.onTimeRate}%</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${carrier.predictedDelayMins > 15 ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'}`}>
                        {carrier.predictedDelayMins} mins
                      </span>
                    </td>
                  </tr>
                )) : (
                  <tr><td colSpan={3} className="px-6 py-4 text-center text-sm text-gray-500">No carriers data found</td></tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
