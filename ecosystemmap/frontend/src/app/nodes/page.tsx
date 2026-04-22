"use client";
import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { api } from '@/lib/api';

export default function NodesPage() {
  const [nodes, setNodes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Replace with a valid ecosystem ID or dynamic fetch in a real app
    api.get('/api/ecosystems/default/nodes')
      .then(res => setNodes(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-8">Loading nodes...</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Nodes</h1>
        <Link href="/" className="text-blue-600 hover:underline">&larr; Back to Dashboard</Link>
      </div>
      <div className="grid gap-4">
        {nodes.map((node: any) => (
          <div key={node.id} className="p-4 border rounded shadow-sm">
            <h3 className="font-semibold text-lg">{node.appName}</h3>
            <p className="mt-2 text-gray-700">Type: {node.nodeType}</p>
            <p className="mt-1 text-sm text-gray-500">Status: {node.status}</p>
          </div>
        ))}
        {nodes.length === 0 && <p>No nodes found.</p>}
      </div>
    </div>
  );
}
