import React from 'react';

export default function ConsolidationOfferDetail({ offer }: { offer: any }) {
  if (!offer) return <div>No offer selected</div>;
  return (
    <div className="p-4 border rounded">
      <h2 className="text-xl font-bold">{offer.name}</h2>
      <p>Status: {offer.status}</p>
    </div>
  );
}
