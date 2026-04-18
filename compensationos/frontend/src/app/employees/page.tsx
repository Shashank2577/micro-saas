"use client";

import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';
import { useState } from 'react';
import BenchmarkModal from '@/components/BenchmarkModal';

export default function EmployeesPage() {
  const [selectedEmpId, setSelectedEmpId] = useState<string | null>(null);

  const { data: employees, isLoading } = useQuery({
    queryKey: ['employees'],
    queryFn: () => api.get('/api/employees').then(res => res.data)
  });

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-900">Employees</h1>
      </div>

      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        <ul className="divide-y divide-gray-200">
          {isLoading ? (
            <li className="p-4 text-center text-gray-500">Loading...</li>
          ) : employees?.length === 0 ? (
            <li className="p-4 text-center text-gray-500">No employees found.</li>
          ) : (
            employees?.map((emp: any) => (
              <li key={emp.id} className="p-4 hover:bg-gray-50 flex justify-between items-center">
                <div>
                  <p className="text-sm font-medium text-indigo-600 truncate">{emp.firstName} {emp.lastName}</p>
                  <p className="text-sm text-gray-500">{emp.role} - {emp.level}</p>
                  <p className="text-sm text-gray-500">Base: ${emp.baseSalary?.toLocaleString()}</p>
                </div>
                <button
                  onClick={() => setSelectedEmpId(emp.id)}
                  className="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded shadow-sm text-white bg-indigo-600 hover:bg-indigo-700"
                >
                  Benchmark
                </button>
              </li>
            ))
          )}
        </ul>
      </div>

      {selectedEmpId && (
        <BenchmarkModal 
          employeeId={selectedEmpId} 
          onClose={() => setSelectedEmpId(null)} 
        />
      )}
    </div>
  );
}
