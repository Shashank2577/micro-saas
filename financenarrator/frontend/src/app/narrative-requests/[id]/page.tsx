'use client';
import { useEffect, useState } from 'react';
import { fetchNarrativeRequest, NarrativeRequest } from '../../../lib/api/narrative-requests';

export default function NarrativeRequestDetail({ params }: { params: { id: string } }) {
  const [data, setData] = useState<NarrativeRequest | null>(null);

  useEffect(() => {
    fetchNarrativeRequest(params.id).then(setData).catch(console.error);
  }, [params.id]);

  if (!data) return <div>Loading...</div>;

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">{data.name}</h1>
      <p>Status: {data.status}</p>
      <pre>{JSON.stringify(data, null, 2)}</pre>
    </div>
  );
}
