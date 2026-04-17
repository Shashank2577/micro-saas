import React from 'react';
import Link from 'next/link';

export default function DashboardPage() {
  return (
    <div className="p-8 space-y-6">
      <h1 className="text-3xl font-bold">PeopleAnalytics Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Link href="/employees" className="p-6 bg-white shadow rounded-lg hover:shadow-md transition">
          <h2 className="text-xl font-semibold">Employees</h2>
          <p className="text-gray-500 mt-2">Manage employee records</p>
        </Link>
        <Link href="/team-health" className="p-6 bg-white shadow rounded-lg hover:shadow-md transition">
          <h2 className="text-xl font-semibold">Team Health</h2>
          <p className="text-gray-500 mt-2">View department metrics</p>
        </Link>
        <Link href="/surveys" className="p-6 bg-white shadow rounded-lg hover:shadow-md transition">
          <h2 className="text-xl font-semibold">Pulse Surveys</h2>
          <p className="text-gray-500 mt-2">Manage feedback</p>
        </Link>
      </div>
    </div>
  );
}
