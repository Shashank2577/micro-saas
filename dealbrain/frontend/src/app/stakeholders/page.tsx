"use client";

import React from 'react';

export default function StakeholdersPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Stakeholders Directory</h1>
      <div className="bg-white p-6 rounded-lg shadow">
        <p className="text-gray-500">Key contacts across pipeline.</p>
        <ul className="mt-4 space-y-3">
          <li className="border-b pb-2">
            <strong>John Doe</strong> - Decision Maker (High Engagement)
          </li>
          <li className="border-b pb-2">
            <strong>Jane Smith</strong> - Champion (Medium Engagement)
          </li>
        </ul>
      </div>
    </div>
  );
}
