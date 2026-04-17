"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { apiClient } from "@/lib/api";

export default function NarrativesPage() {
  const [narratives, setNarratives] = useState<any[]>([]);

  useEffect(() => {
    apiClient("/api/v1/narratives", { headers: { "X-Tenant-ID": "tenant-1" } })
      .then((res) => res.json())
      .then(data => setNarratives(data.content || []))
      .catch(console.error);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Narratives</h1>
        <Link href="/narratives/new">
          <button className="bg-blue-600 text-white px-4 py-2 rounded">Generate New</button>
        </Link>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {narratives?.map((n) => (
          <Link key={n.id} href={`/narratives/${n.id}`}>
            <div className="p-6 border rounded-lg hover:shadow-lg transition">
              <h2 className="text-xl font-semibold mb-2">{n.title}</h2>
              <span className={`px-2 py-1 text-xs rounded ${n.status === 'PUBLISHED' ? 'bg-green-100 text-green-800' : 'bg-gray-100'}`}>{n.status}</span>
              <p className="mt-4 text-sm text-gray-600 line-clamp-3">{n.contentMarkdown}</p>
            </div>
          </Link>
        ))}
        {narratives.length === 0 && <p className="text-gray-500">No narratives found.</p>}
      </div>
    </div>
  );
}
