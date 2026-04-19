"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchWithTenant } from "@/lib/apiClient";
import Link from "next/link";
import { ArrowLeft } from "lucide-react";

export default function TaskAssignmentDetail({ params }: { params: { id: string } }) {
  const router = useRouter();
  const isNew = params.id === "new";
  const [formData, setFormData] = useState({ name: "", status: "NEW", metadataJson: "{}" });

  useEffect(() => {
    if (!isNew) {
      fetchWithTenant(`/task-assignments/${params.id}`).then((data) => {
        setFormData({
          name: data.name || "",
          status: data.status || "NEW",
          metadataJson: data.metadataJson || "{}",
        });
      });
    }
  }, [isNew, params.id]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (isNew) {
        await fetchWithTenant("/task-assignments", {
          method: "POST",
          body: JSON.stringify(formData),
        });
      } else {
        await fetchWithTenant(`/task-assignments/${params.id}`, {
          method: "PATCH",
          body: JSON.stringify(formData),
        });
      }
      router.push("/task-assignments");
    } catch (err) {
      console.error(err);
      alert("Failed to save.");
    }
  };

  const handleValidate = async () => {
    if (isNew) return;
    try {
      await fetchWithTenant(`/task-assignments/${params.id}/validate`, { method: "POST" });
      router.push("/task-assignments");
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-8 max-w-3xl mx-auto">
      <Link href="/task-assignments" className="text-gray-600 hover:text-gray-900 flex items-center mb-6">
        <ArrowLeft className="w-4 h-4 mr-2" /> Back to Tasks
      </Link>
      <h1 className="text-2xl font-bold mb-6">{isNew ? "New Task Assignment" : "Edit Task"}</h1>

      <form onSubmit={handleSubmit} className="space-y-6 bg-white p-6 rounded-lg shadow">
        <div>
          <label className="block text-sm font-medium text-gray-700">Name</label>
          <input
            type="text"
            required
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Metadata (JSON)</label>
          <textarea
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border font-mono"
            rows={5}
            value={formData.metadataJson}
            onChange={(e) => setFormData({ ...formData, metadataJson: e.target.value })}
          />
        </div>
        <div className="flex justify-between">
          <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
            Save
          </button>
          {!isNew && (
            <button
              type="button"
              onClick={handleValidate}
              className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700"
            >
              Validate
            </button>
          )}
        </div>
      </form>
    </div>
  );
}
