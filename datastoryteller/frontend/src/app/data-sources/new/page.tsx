"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiClient } from "@/lib/api";

export default function NewDataSource() {
  const router = useRouter();
  const [formData, setFormData] = useState({ name: "", type: "POSTGRES", status: "ACTIVE" });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await apiClient("/api/v1/data-sources", {
      method: "POST",
      headers: { "X-Tenant-ID": "tenant-1", "Content-Type": "application/json" },
      body: JSON.stringify(formData)
    });
    router.push("/data-sources");
  };

  return (
    <div className="p-8 max-w-lg">
      <h1 className="text-3xl font-bold mb-6">New Data Source</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block mb-1">Name</label>
          <input className="border p-2 w-full rounded" required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
        </div>
        <div>
          <label className="block mb-1">Type</label>
          <select className="border p-2 w-full rounded" value={formData.type} onChange={e => setFormData({...formData, type: e.target.value})}>
            <option value="POSTGRES">PostgreSQL</option>
            <option value="SNOWFLAKE">Snowflake</option>
            <option value="BQ">BigQuery</option>
            <option value="UPLOAD">Upload</option>
          </select>
        </div>
        <button className="bg-blue-600 text-white px-4 py-2 rounded">Save</button>
      </form>
    </div>
  );
}
