import React from 'react';

export default function PlanDetail({ plan }: { plan: any }) {
  if (!plan) return <div>No plan selected</div>;
  return (
    <div className="p-4 border rounded">
      <h2 className="text-xl font-bold">{plan.name}</h2>
      <p>Status: {plan.status}</p>
    </div>
  );
}
