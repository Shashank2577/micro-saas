import React from 'react';

export default function AccountDetail({ account }: { account: any }) {
  if (!account) return <div>No account selected</div>;
  return (
    <div className="p-4 border rounded">
      <h2 className="text-xl font-bold">{account.name}</h2>
      <p>Status: {account.status}</p>
    </div>
  );
}
