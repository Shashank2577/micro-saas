'use client';
import { useEffect, useState } from 'react';
import { fetchNarrativeSection, NarrativeSection } from '../../../lib/api/narrative-sections';

export default function NarrativeSectionDetail({ params }: { params: { id: string } }) {
  const [data, setData] = useState<NarrativeSection | null>(null);

  useEffect(() => {
    fetchNarrativeSection(params.id).then(setData).catch(console.error);
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
