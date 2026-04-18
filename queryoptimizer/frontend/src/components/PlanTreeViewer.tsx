"use client";

import React from 'react';

export default function PlanTreeViewer({ plan }: { plan: any }) {
  if (!plan) return <div className="text-gray-500">No execution plan available.</div>;
  if (plan.error) return <div className="text-red-500">Error parsing plan: {plan.error}</div>;

  return (
    <div className="bg-gray-100 p-4 rounded overflow-auto border">
      <pre className="text-xs text-gray-800 font-mono">
        {JSON.stringify(plan, null, 2)}
      </pre>
    </div>
  );
}
