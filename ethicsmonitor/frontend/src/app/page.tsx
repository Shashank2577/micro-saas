"use client";

import { useEffect, useState } from "react";
import { Model } from "@/types/index";

export default function Home() {
  const [models, setModels] = useState<Model[]>([]);

  useEffect(() => {
    fetch("http://localhost:8160/api/models", {
      headers: {
        "X-Tenant-ID": "test-tenant-123"
      }
    })
      .then(res => res.json())
      .then(data => {
         if (Array.isArray(data)) {
           setModels(data);
         }
      })
      .catch(console.error);
  }, []);

  return (
    <main className="min-h-screen p-8 text-white bg-slate-900">
      <h1 className="text-4xl font-bold mb-8">EthicsMonitor Dashboard</h1>
      <div className="grid grid-cols-1 gap-6">
        {models.map((model) => (
          <div key={model.id} className="p-6 bg-slate-800 rounded-lg shadow-md border border-slate-700">
            <h2 className="text-2xl font-semibold text-blue-400">{model.name}</h2>
            <p className="mt-2 text-slate-300">Purpose: {model.purpose}</p>
            <p className="mt-1 text-slate-300">Risk Tier: <span className="font-medium text-yellow-400">{model.riskTier}</span></p>
            <p className="mt-1 text-slate-300">Status: {model.status} | Version: {model.version}</p>
            <div className="mt-4 flex gap-4">
              <button className="px-4 py-2 bg-blue-600 hover:bg-blue-700 rounded transition">View Details</button>
            </div>
          </div>
        ))}
      </div>
    </main>
  );
}
