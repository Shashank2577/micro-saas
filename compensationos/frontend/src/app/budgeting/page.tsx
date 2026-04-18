"use client";

import { useQuery, useMutation } from '@tanstack/react-query';
import api from '@/lib/api';
import { useState } from 'react';

export default function BudgetingPage() {
  const [scenarioParams, setScenarioParams] = useState({ increasePercent: 5, budgetCap: 500000 });

  const { data: cycles, isLoading } = useQuery({
    queryKey: ['cycles'],
    queryFn: () => api.get('/api/cycles').then(res => res.data)
  });

  const modelScenarioMutation = useMutation({
    mutationFn: (data: { cycleId: string; params: any }) => 
      api.post(`/api/cycles/${data.cycleId}/model-scenario`, data.params).then(res => res.data)
  });

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900">Compensation Budgeting</h1>

      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white shadow sm:rounded-lg p-6">
            <h2 className="text-xl font-medium mb-4">Cycles</h2>
            <ul className="divide-y divide-gray-200">
              {cycles?.length === 0 ? <li className="text-gray-500 py-2">No cycles found.</li> : null}
              {cycles?.map((cycle: any) => (
                <li key={cycle.id} className="py-4">
                  <div className="flex justify-between">
                    <div>
                      <p className="text-sm font-medium text-gray-900">{cycle.cycleName}</p>
                      <p className="text-sm text-gray-500">Status: {cycle.status}</p>
                    </div>
                    <button
                      onClick={() => modelScenarioMutation.mutate({ cycleId: cycle.id, params: scenarioParams })}
                      className="text-indigo-600 hover:text-indigo-900 text-sm font-medium"
                    >
                      Run Scenario
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          </div>

          <div className="bg-white shadow sm:rounded-lg p-6">
            <h2 className="text-xl font-medium mb-4">Scenario Parameters</h2>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Increase %</label>
                <input
                  type="number"
                  value={scenarioParams.increasePercent}
                  onChange={(e) => setScenarioParams({ ...scenarioParams, increasePercent: Number(e.target.value) })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Budget Cap</label>
                <input
                  type="number"
                  value={scenarioParams.budgetCap}
                  onChange={(e) => setScenarioParams({ ...scenarioParams, budgetCap: Number(e.target.value) })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2 border"
                />
              </div>

              {modelScenarioMutation.isSuccess && (
                <div className="mt-4 bg-green-50 p-4 rounded-md">
                  <h4 className="text-sm font-medium text-green-800 mb-2">Scenario Results</h4>
                  <p className="text-sm text-green-700">Projected Cost: ${modelScenarioMutation.data.projectedTotalCost?.toLocaleString()}</p>
                  <p className="text-sm text-green-700">Remaining Budget: ${modelScenarioMutation.data.remainingBudget?.toLocaleString()}</p>
                  <p className="text-sm text-green-700">Eligible Employees: {modelScenarioMutation.data.eligibleEmployees}</p>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
