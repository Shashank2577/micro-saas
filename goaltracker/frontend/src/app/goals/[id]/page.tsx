"use client";

import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useParams } from "next/navigation";
import { useState } from "react";

export default function GoalDetails() {
  const { id } = useParams();
  const queryClient = useQueryClient();
  const [contribution, setContribution] = useState("");

  const { data: goal, isLoading } = useQuery({
    queryKey: ["goal", id],
    queryFn: async () => {
      const res = await fetch(`/api/v1/goals/${id}`, {
        headers: { "X-Tenant-ID": "tenant-1" }
      });
      return res.json();
    }
  });

  const { data: nudge } = useQuery({
    queryKey: ["goal-nudge", id],
    queryFn: async () => {
      const res = await fetch(`/api/v1/goals/${id}/nudge`, {
        headers: { "X-Tenant-ID": "tenant-1" }
      });
      return res.json();
    }
  });

  const { data: savingsPlan } = useQuery({
    queryKey: ["goal-savings-plan", id],
    queryFn: async () => {
      const res = await fetch(`/api/v1/goals/${id}/savings-plan`, {
        headers: { "X-Tenant-ID": "tenant-1" }
      });
      return res.json();
    }
  });

  const mutation = useMutation({
    mutationFn: async (amount: string) => {
      return fetch(`/api/v1/goals/${id}/contributions`, {
        method: "POST",
        headers: { "Content-Type": "application/json", "X-Tenant-ID": "tenant-1" },
        body: JSON.stringify({ amount: parseFloat(amount), type: "manual" })
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["goal", id] });
      setContribution("");
    }
  });

  if (isLoading) return <div className="p-8">Loading goal details...</div>;

  const progress = Math.min(100, (goal.currentAmount / goal.targetAmount) * 100);

  return (
    <div className="max-w-4xl mx-auto p-8 space-y-8">
      <div className="bg-white p-6 rounded-lg shadow">
        <h1 className="text-3xl font-bold">{goal.title}</h1>
        <p className="text-gray-500 mb-4">{goal.category} • Target: ${goal.targetAmount} by {new Date(goal.deadline).toLocaleDateString()}</p>

        <div className="w-full bg-gray-200 rounded-full h-4 mb-2">
          <div className="bg-green-500 h-4 rounded-full transition-all duration-500" style={{ width: `${progress}%` }}></div>
        </div>
        <p className="text-right font-semibold">${goal.currentAmount} / ${goal.targetAmount} ({progress.toFixed(1)}%)</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-blue-50 p-6 rounded-lg">
          <h2 className="text-xl font-bold mb-2">AI Nudge</h2>
          <p className="italic">"{nudge?.nudge || 'Loading nudge...'}"</p>
        </div>

        <div className="bg-indigo-50 p-6 rounded-lg">
          <h2 className="text-xl font-bold mb-2">Savings Plan</h2>
          <p>Recommended Monthly Contribution: <span className="font-bold">${savingsPlan?.monthlySavingsRequired || '...'}</span></p>
        </div>
      </div>

      <div className="bg-white p-6 rounded-lg shadow">
        <h2 className="text-xl font-bold mb-4">Add Contribution</h2>
        <div className="flex gap-4">
          <input
            type="number"
            value={contribution}
            onChange={e => setContribution(e.target.value)}
            placeholder="Amount"
            className="border p-2 rounded"
          />
          <button
            onClick={() => mutation.mutate(contribution)}
            disabled={!contribution || mutation.isPending}
            className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
          >
            {mutation.isPending ? 'Adding...' : 'Add'}
          </button>
        </div>
      </div>
    </div>
  );
}
