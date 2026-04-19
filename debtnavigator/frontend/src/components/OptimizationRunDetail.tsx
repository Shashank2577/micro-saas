import React from 'react';

export default function OptimizationRunDetail({ run }: { run: any }) {
  if (!run) return <div>No run selected</div>;
  return (
    <div className="p-4 border rounded">
      <h2 className="text-xl font-bold">{run.name}</h2>
      <p>Status: {run.status}</p>
    </div>
  );
}
