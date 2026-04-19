import React from 'react';

export default function RiskProjectionDetail({ projection }: { projection: any }) {
  if (!projection) return <div>No projection selected</div>;
  return (
    <div className="p-4 border rounded">
      <h2 className="text-xl font-bold">{projection.name}</h2>
      <p>Status: {projection.status}</p>
    </div>
  );
}
