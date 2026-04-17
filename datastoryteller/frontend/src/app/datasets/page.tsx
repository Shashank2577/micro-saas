"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { apiClient } from "@/lib/api";

export default function DatasetsPage() {
  const [datasets, setDatasets] = useState<any[]>([]);

  useEffect(() => {
    apiClient("/api/v1/datasets", { headers: { "X-Tenant-ID": "tenant-1" } })
      .then((res) => res.json())
      .then(setDatasets)
      .catch(console.error);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Datasets</h1>
        <button className="bg-blue-600 text-white px-4 py-2 rounded">Register Dataset</button>
      </div>
      <ul className="space-y-4">
        {datasets?.map((d) => (
          <li key={d.id} className="p-4 border rounded hover:shadow transition">
            <Link href={`/datasets/${d.id}`}>
              <div className="text-xl font-semibold">{d.name}</div>
              <div className="text-gray-500 font-mono text-sm">{d.sqlQuery}</div>
            </Link>
          </li>
        ))}
        {datasets.length === 0 && <p className="text-gray-500">No datasets registered.</p>}
      </ul>
    </div>
  );
}
