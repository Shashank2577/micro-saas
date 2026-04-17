"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function CreateGoal() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    title: "",
    category: "Emergency Fund",
    targetAmount: "",
    deadline: "",
    priority: "1",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const res = await fetch("/api/v1/goals", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-Tenant-ID": "tenant-1",
      },
      body: JSON.stringify({
        ...formData,
        targetAmount: parseFloat(formData.targetAmount),
        deadline: new Date(formData.deadline).toISOString(),
        priority: parseInt(formData.priority, 10),
      }),
    });

    if (res.ok) {
      router.push("/goals");
    }
  };

  return (
    <div className="max-w-xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Create a New Goal</h1>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium">Title</label>
          <input
            type="text"
            value={formData.title}
            onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            className="w-full border p-2 rounded text-black"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium">Category</label>
          <select
            value={formData.category}
            onChange={(e) => setFormData({ ...formData, category: e.target.value })}
            className="w-full border p-2 rounded text-black"
          >
            <option>Emergency Fund</option>
            <option>Vacation</option>
            <option>Car</option>
            <option>Home Down Payment</option>
            <option>Other</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium">Target Amount ($)</label>
          <input
            type="number"
            value={formData.targetAmount}
            onChange={(e) => setFormData({ ...formData, targetAmount: e.target.value })}
            className="w-full border p-2 rounded text-black"
            required
            min="1"
          />
        </div>
        <div>
          <label className="block text-sm font-medium">Deadline</label>
          <input
            type="date"
            value={formData.deadline}
            onChange={(e) => setFormData({ ...formData, deadline: e.target.value })}
            className="w-full border p-2 rounded text-black"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium">Priority (1-5)</label>
          <input
            type="number"
            value={formData.priority}
            onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
            className="w-full border p-2 rounded text-black"
            required
            min="1"
            max="5"
          />
        </div>
        <button type="submit" className="w-full bg-blue-500 text-white p-2 rounded">
          Create Goal
        </button>
      </form>
    </div>
  );
}
