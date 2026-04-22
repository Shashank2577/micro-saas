'use client';

import React from 'react';

export default function ApprovalsPage() {
  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-semibold text-gray-900">Approvals</h1>
      </div>

      <div className="bg-white shadow overflow-hidden sm:rounded-md p-6">
        <p className="text-gray-500">Approval workflows and pending actions will be displayed here.</p>
      </div>
    </div>
  );
}
