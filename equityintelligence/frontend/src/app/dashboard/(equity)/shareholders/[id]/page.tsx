'use client'

import React, { useState, useEffect } from 'react';

export default function ShareholdersDetailPage({ params }: { params: { id: string } }) {
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(`/api/v1/equity/shareholders/${params.id}`)
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch');
        return res.json();
      })
      .then(data => {
        setData(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, [params.id]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!data) return <div>Not found</div>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Shareholders Detail: {data.name}</h1>
      <div className="bg-white shadow rounded p-4">
        <p><strong>Status:</strong> {data.status}</p>
        <p><strong>Created:</strong> {new Date(data.createdAt).toLocaleDateString()}</p>
      </div>
    </div>
  );
}
