"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { apiClient } from "@/lib/api";

export default function DataSourcesPage() {
  const [sources, setSources] = useState<any[]>([]);

  useEffect(() => {
    apiClient("/api/v1/data-sources", { headers: { "X-Tenant-ID": "tenant-1" } })
      .then((res) => res.json())
      .then(setSources)
      .catch(console.error);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Data Sources</h1>
        <Link href="/data-sources/new">
          <button className="bg-blue-600 text-white px-4 py-2 rounded">Add New</button>
        </Link>
      </div>
      <table className="w-full border-collapse">
        <thead>
          <tr className="border-b">
            <th className="text-left py-2">Name</th>
            <th className="text-left py-2">Type</th>
            <th className="text-left py-2">Status</th>
          </tr>
        </thead>
        <tbody>
          {sources?.map((s) => (
            <tr key={s.id} className="border-b">
              <td className="py-2">{s.name}</td>
              <td className="py-2">{s.type}</td>
              <td className="py-2">{s.status}</td>
            </tr>
          ))}
          {sources.length === 0 && <tr><td colSpan={3} className="py-4 text-center text-gray-500">No data sources found.</td></tr>}
        </tbody>
      </table>
    </div>
  );
}
