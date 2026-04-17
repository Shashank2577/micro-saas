"use client";

import { useState } from "react";
import { AppSidebar } from "@/components/app-sidebar";

export default function PoliciesPage() {
  const [policies, setPolicies] = useState([
    { id: 1, name: "Daily Backup", schedule: "0 2 * * *", retention: "7 days", status: "Active" }
  ]);

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <AppSidebar />
      <div className="flex-1 overflow-auto">
        <header className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 px-8 py-6">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Backup Policies</h1>
            <button className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">Create Policy</button>
          </div>
        </header>
        <main className="p-8 max-w-7xl mx-auto space-y-8">
          <div className="bg-white dark:bg-gray-800 p-6 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm">
            <h2 className="text-lg font-semibold mb-4">Configured Policies</h2>
            <div className="space-y-4">
              {policies.map(p => (
                <div key={p.id} className="flex justify-between items-center border-b pb-2">
                  <div>
                    <p className="font-medium text-gray-900 dark:text-white">{p.name}</p>
                    <p className="text-sm text-gray-500">{p.schedule} • Retain {p.retention}</p>
                  </div>
                  <span className="text-green-600 bg-green-100 px-2 py-1 rounded-full text-xs font-semibold">{p.status}</span>
                </div>
              ))}
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
