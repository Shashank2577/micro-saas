'use client';

import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { api } from '@/lib/api';

export default function CustomersPage() {
  const [customers, setCustomers] = useState<any[]>([]);

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      const res = await api.get('/api/v1/customers');
      setCustomers(res.data);
    } catch (err) {
      console.error('Failed to fetch customers', err);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Customers</h1>
      </div>

      <Card>
        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <table className="w-full text-sm text-left">
              <thead className="text-xs uppercase bg-gray-50 border-b">
                <tr>
                  <th className="px-6 py-3">Name</th>
                  <th className="px-6 py-3">Industry</th>
                  <th className="px-6 py-3">MRR</th>
                  <th className="px-6 py-3">Health Score</th>
                  <th className="px-6 py-3">Risk Segment</th>
                </tr>
              </thead>
              <tbody>
                {customers.map((c) => (
                  <tr key={c.id} className="bg-white border-b hover:bg-gray-50">
                    <td className="px-6 py-4 font-medium">{c.name}</td>
                    <td className="px-6 py-4">{c.industry}</td>
                    <td className="px-6 py-4">${c.mrr}</td>
                    <td className="px-6 py-4">{c.latestHealthScore?.compositeScore || 'N/A'}</td>
                    <td className="px-6 py-4">
                      <span className={`px-2 py-1 rounded text-xs font-semibold
                        ${c.latestPrediction?.riskSegment === 'HIGH' ? 'bg-red-100 text-red-800' : 
                          c.latestPrediction?.riskSegment === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' : 
                          c.latestPrediction?.riskSegment === 'LOW' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                        {c.latestPrediction?.riskSegment || 'UNKNOWN'}
                      </span>
                    </td>
                  </tr>
                ))}
                {customers.length === 0 && (
                  <tr>
                    <td colSpan={5} className="px-6 py-4 text-center text-gray-500">No customers found</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
