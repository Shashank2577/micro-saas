"use client";

import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';

export default function BenchmarkModal({ employeeId, onClose }: { employeeId: string, onClose: () => void }) {
  const { data: benchmark, isLoading } = useQuery({
    queryKey: ['benchmark', employeeId],
    queryFn: () => api.get(`/api/employees/${employeeId}/benchmark`).then(res => res.data)
  });

  return (
    <div className="fixed z-10 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div className="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" onClick={onClose}></div>
        <span className="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
        <div className="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
          <div>
            <div className="mt-3 text-center sm:mt-5">
              <h3 className="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                Compensation Benchmark
              </h3>
              <div className="mt-2 text-left">
                {isLoading ? (
                  <p className="text-sm text-gray-500">Loading...</p>
                ) : benchmark ? (
                  <div className="space-y-4">
                    <p className="text-sm text-gray-500">
                      <strong>Role:</strong> {benchmark.employee?.role} ({benchmark.employee?.level})
                    </p>
                    <p className="text-sm text-gray-500">
                      <strong>Current Base:</strong> ${benchmark.employee?.baseSalary?.toLocaleString()}
                    </p>
                    {benchmark.marketData ? (
                      <>
                        <div className="bg-gray-50 p-4 rounded-md">
                          <h4 className="font-medium text-gray-900 mb-2">Market Data</h4>
                          <ul className="text-sm text-gray-600">
                            <li>25th Percentile: ${benchmark.marketData.p25Salary?.toLocaleString()}</li>
                            <li>50th Percentile: ${benchmark.marketData.p50Salary?.toLocaleString()}</li>
                            <li>75th Percentile: ${benchmark.marketData.p75Salary?.toLocaleString()}</li>
                            <li>90th Percentile: ${benchmark.marketData.p90Salary?.toLocaleString()}</li>
                          </ul>
                        </div>
                        <p className="text-sm text-gray-500">
                          <strong>Compa-Ratio:</strong> {benchmark.compaRatio}
                        </p>
                        <p className="text-sm text-gray-500">
                          <strong>Range Penetration:</strong> {benchmark.rangePenetration}
                        </p>
                      </>
                    ) : (
                      <p className="text-sm text-red-500">No market data available for this role and location.</p>
                    )}
                  </div>
                ) : (
                  <p className="text-sm text-red-500">Failed to load benchmark data.</p>
                )}
              </div>
            </div>
          </div>
          <div className="mt-5 sm:mt-6">
            <button
              type="button"
              className="inline-flex justify-center w-full rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none sm:text-sm"
              onClick={onClose}
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
