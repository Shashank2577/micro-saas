"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { apiClient } from "@/lib/api";

export default function NarrativeDetail() {
  const params = useParams();
  const [narrative, setNarrative] = useState<any>(null);

  useEffect(() => {
    if (params.id) {
      apiClient(`/api/v1/narratives/${params.id}`, { headers: { "X-Tenant-ID": "tenant-1" } })
        .then(res => res.json())
        .then(setNarrative)
        .catch(console.error);
    }
  }, [params.id]);

  const handlePublish = async () => {
    await apiClient(`/api/v1/narratives/${params.id}/publish`, { method: "PUT", headers: { "X-Tenant-ID": "tenant-1" } });
    setNarrative({...narrative, status: "PUBLISHED"});
  };

  if (!narrative) return <div className="p-8">Loading...</div>;

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">{narrative.title}</h1>
        {narrative.status !== 'PUBLISHED' && (
          <button onClick={handlePublish} className="bg-green-600 text-white px-4 py-2 rounded">Publish</button>
        )}
      </div>
      <div className="mb-4">
        <span className={`px-2 py-1 text-xs rounded ${narrative.status === 'PUBLISHED' ? 'bg-green-100 text-green-800' : 'bg-gray-100'}`}>
          {narrative.status}
        </span>
      </div>
      <div className="prose max-w-none border p-8 rounded bg-white shadow-sm whitespace-pre-wrap">
        {narrative.contentMarkdown}
      </div>

      {narrative.status === 'PUBLISHED' && (
        <div className="mt-8 p-4 bg-gray-50 border rounded">
          <h3 className="font-semibold mb-2">Rate this insight</h3>
          <div className="flex gap-2">
            {[1,2,3,4,5].map(rating => (
              <button key={rating} className="px-3 py-1 bg-white border rounded hover:bg-gray-100">{rating} ★</button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
