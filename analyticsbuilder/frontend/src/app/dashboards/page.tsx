"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { fetchApi } from "@/lib/api";

export default function DashboardsPage() {
  const [dashboards, setDashboards] = useState<any[]>([]);

  useEffect(() => {
    async function loadDashboards() {
      const data = await fetchApi("/api/v1/dashboards");
      setDashboards(data);
    }
    loadDashboards();
  }, []);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Dashboards</h1>
      <Link href="/dashboards/new" className="bg-blue-500 text-white px-4 py-2 rounded mb-4 inline-block">
        Create Dashboard
      </Link>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {dashboards.map((dashboard) => (
          <div key={dashboard.id} className="border p-4 rounded shadow">
            <h2 className="text-xl font-semibold">{dashboard.name}</h2>
            <p className="text-gray-600">{dashboard.description}</p>
            <Link href={`/dashboards/${dashboard.id}`} className="text-blue-500 mt-2 inline-block">
              View Dashboard
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
}
