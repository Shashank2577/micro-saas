"use client";

import { useQuery, useMutation } from '@tanstack/react-query';
import api from '@/lib/api';
import { useState } from 'react';

export default function EquityModelingPage() {
  const [calcParams, setCalcParams] = useState({ planId: '', shares: 1000, vestingStartDate: new Date().toISOString().split('T')[0] });

  const { data: plans, isLoading: plansLoading } = useQuery({
    queryKey: ['equityPlans'],
    queryFn: () => api.get('/api/equity').then(res => res.data)
  });

  const calcMutation = useMutation({
    mutationFn: (data: any) => api.post('/api/equity/grant-calculator', data).then(res => res.data)
  });

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900">Equity Modeling</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white shadow sm:rounded-lg p-6 space-y-4">
          <h2 className="text-xl font-medium">Grant Calculator</h2>
          
          {plansLoading ? <p>Loading plans...</p> : (
            <>
              <div>
                <label className="block text-sm font-medium text-gray-700">Select Plan</label>
                <select
                  value={calcParams.planId}
                  onChange={(e) => setCalcParams({ ...calcParams, planId: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                >
                  <option value="">-- Select --</option>
                  {plans?.map((plan: any) => (
                    <option key={plan.id} value={plan.id}>{plan.planName} (Val: ${plan.currentValuation})</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Shares</label>
                <input
                  type="number"
                  value={calcParams.shares}
                  onChange={(e) => setCalcParams({ ...calcParams, shares: Number(e.target.value) })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Start Date</label>
                <input
                  type="date"
                  value={calcParams.vestingStartDate}
                  onChange={(e) => setCalcParams({ ...calcParams, vestingStartDate: e.target.value })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                />
              </div>

              <button
                onClick={() => calcMutation.mutate(calcParams)}
                disabled={!calcParams.planId || calcMutation.isPending}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:bg-gray-400"
              >
                {calcMutation.isPending ? 'Calculating...' : 'Calculate'}
              </button>
            </>
          )}
        </div>

        <div className="bg-white shadow sm:rounded-lg p-6">
          <h2 className="text-xl font-medium mb-4">Vesting Schedule</h2>
          {calcMutation.isSuccess ? (
            <div>
              <p className="text-sm text-gray-600 mb-4">Total Value: ${calcMutation.data.totalValue?.toLocaleString()}</p>
              <div className="max-h-96 overflow-y-auto border border-gray-200 rounded">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                      <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Shares</th>
                      <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Value</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200 text-sm">
                    {calcMutation.data.vestingSchedule?.map((event: any, i: number) => (
                      <tr key={i}>
                        <td className="px-4 py-2 whitespace-nowrap text-gray-900">{event.date}</td>
                        <td className="px-4 py-2 whitespace-nowrap text-gray-500">{event.sharesVesting}</td>
                        <td className="px-4 py-2 whitespace-nowrap text-gray-500">${event.valueVesting?.toLocaleString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ) : (
            <p className="text-gray-500 text-sm">Fill out the form and calculate to view schedule.</p>
          )}
        </div>
      </div>
    </div>
  );
}
