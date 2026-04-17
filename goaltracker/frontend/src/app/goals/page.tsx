"use client";

import { useQuery } from "@tanstack/react-query";
import Link from "next/link";

export default function GoalsList() {
  const { data: goals, isLoading } = useQuery({
    queryKey: ["goals"],
    queryFn: async () => {
      const res = await fetch("/api/v1/goals", {
        headers: { "X-Tenant-ID": "tenant-1" }
      });
      return res.json();
    }
  });

  if (isLoading) return <div className="p-8">Loading goals...</div>;

  return (
    <div className="max-w-4xl mx-auto p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Your Goals</h1>
        <Link href="/goals/create" className="bg-blue-600 text-white px-4 py-2 rounded">
          Create Goal
        </Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {goals?.map((goal: any) => (
          <Link key={goal.id} href={`/goals/${goal.id}`} className="block bg-white p-6 rounded-lg shadow hover:shadow-md">
            <h2 className="text-xl font-bold mb-2">{goal.title}</h2>
            <p className="text-gray-600">{goal.category}</p>
            <div className="mt-4">
              <div className="w-full bg-gray-200 rounded-full h-2.5">
                <div
                  className="bg-blue-600 h-2.5 rounded-full"
                  style={{ width: `${Math.min(100, (goal.currentAmount / goal.targetAmount) * 100)}%` }}
                ></div>
              </div>
              <p className="text-sm mt-2">
                ${goal.currentAmount} / ${goal.targetAmount}
              </p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}
