'use client';
import { useEffect, useState } from 'react';
import { fetchExportArtifact, ExportArtifact } from '../../../lib/api/exports';

export default function ExportArtifactDetail({ params }: { params: { id: string } }) {
  const [data, setData] = useState<ExportArtifact | null>(null);

  useEffect(() => {
    fetchExportArtifact(params.id).then(setData).catch(console.error);
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
