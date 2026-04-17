"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { apiClient } from "@/lib/api";

export default function NewNarrative() {
  const router = useRouter();
  const [datasets, setDatasets] = useState<any[]>([]);
  const [templates, setTemplates] = useState<any[]>([]);
  const [datasetId, setDatasetId] = useState("");
  const [templateId, setTemplateId] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    apiClient("/api/v1/datasets", { headers: { "X-Tenant-ID": "tenant-1" } }).then(res => res.json()).then(setDatasets);
    apiClient("/api/v1/templates", { headers: { "X-Tenant-ID": "tenant-1" } }).then(res => res.json()).then(setTemplates);
  }, []);

  const handleGenerate = async () => {
    setLoading(true);
    try {
      const res = await apiClient("/api/v1/narratives/generate", {
        method: "POST",
        headers: { "X-Tenant-ID": "tenant-1", "Content-Type": "application/json" },
        body: JSON.stringify({ datasetId, templateId })
      });
      const data = await res.json();
      router.push(`/narratives/${data.id}`);
    } catch (e) {
      console.error(e);
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-lg">
      <h1 className="text-3xl font-bold mb-6">Generate Narrative</h1>
      <div className="space-y-4">
        <div>
          <label className="block mb-1">Select Dataset</label>
          <select className="border p-2 w-full rounded" value={datasetId} onChange={e => setDatasetId(e.target.value)}>
            <option value="">-- Select --</option>
            {datasets.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
          </select>
        </div>
        <div>
          <label className="block mb-1">Select Template</label>
          <select className="border p-2 w-full rounded" value={templateId} onChange={e => setTemplateId(e.target.value)}>
            <option value="">-- Select --</option>
            {templates.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
          </select>
        </div>
        <button
          onClick={handleGenerate}
          disabled={!datasetId || !templateId || loading}
          className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
        >
          {loading ? "Generating..." : "Generate Narrative"}
        </button>
      </div>
    </div>
  );
}
