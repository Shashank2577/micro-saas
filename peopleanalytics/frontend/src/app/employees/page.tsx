"use client";

import React from 'react';
import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';

export default function EmployeesPage() {
  const { data: employees, isLoading } = useQuery({
    queryKey: ['employees'],
    queryFn: async () => {
      const res = await api.get('/employees');
      return res.data;
    },
  });

  if (isLoading) return <div>Loading employees...</div>;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Employees</h1>
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-gray-50 border-b border-gray-100">
            <tr>
              <th className="p-4 font-semibold text-gray-600">ID</th>
              <th className="p-4 font-semibold text-gray-600">Department</th>
              <th className="p-4 font-semibold text-gray-600">Role</th>
              <th className="p-4 font-semibold text-gray-600">Status</th>
            </tr>
          </thead>
          <tbody>
            {employees?.map((emp: any) => (
              <tr key={emp.id} className="border-b border-gray-50 hover:bg-gray-50 transition">
                <td className="p-4">{emp.id}</td>
                <td className="p-4">{emp.department}</td>
                <td className="p-4">{emp.role}</td>
                <td className="p-4">
                  <span className={`px-2 py-1 rounded text-xs ${emp.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                    {emp.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
