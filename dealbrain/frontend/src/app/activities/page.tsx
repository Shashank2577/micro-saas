"use client";

import React from 'react';

export default function ActivitiesPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Deal Activities</h1>
      <div className="bg-white p-6 rounded-lg shadow">
        <p className="text-gray-500">Recent activities across all deals.</p>
        <ul className="mt-4 space-y-3">
          <li className="border-b pb-2">
            <span className="text-sm text-gray-400">10 mins ago</span> - Email sent to John Doe
          </li>
          <li className="border-b pb-2">
            <span className="text-sm text-gray-400">1 hour ago</span> - Call logged for Deal #1234
          </li>
        </ul>
      </div>
    </div>
  );
}
