"use client";
import React, { useEffect, useState } from 'react';

export default function EmployeesPage() {
  const [employees, setEmployees] = useState([]);

  useEffect(() => {
    fetch('/api/employees', { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
      .then(res => res.json())
      .then(data => setEmployees(data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Employees</h1>
      <table className="min-w-full bg-white shadow rounded">
        <thead>
          <tr className="border-b">
            <th className="py-2 px-4 text-left">Name</th>
            <th className="py-2 px-4 text-left">Email</th>
            <th className="py-2 px-4 text-left">Department</th>
            <th className="py-2 px-4 text-left">Role</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((emp: any) => (
            <tr key={emp.id} className="border-b">
              <td className="py-2 px-4">{emp.firstName} {emp.lastName}</td>
              <td className="py-2 px-4">{emp.email}</td>
              <td className="py-2 px-4">{emp.department}</td>
              <td className="py-2 px-4">{emp.role}</td>
            </tr>
          ))}
          {employees.length === 0 && (
            <tr><td colSpan={4} className="py-4 text-center text-gray-500">No employees found.</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
