"use client";

import { useState } from "react";
import { AppSidebar } from "@/components/app-sidebar";

export default function RestoresPage() {
  const [restores, setRestores] = useState([
    { id: 1, executionId: "exec-123", targetEnv: "staging", pointInTime: "2026-04-16T12:00:00Z", status: "Success", time: "1 day ago" }
  ]);

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <AppSidebar />
      <div className="flex-1 overflow-auto">
        <header className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 px-8 py-6">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Restore Jobs</h1>
            <button className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">Initiate Restore</button>
          </div>
        </header>
        <main className="p-8 max-w-7xl mx-auto space-y-8">
          <div className="bg-white dark:bg-gray-800 p-6 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm">
            <h2 className="text-lg font-semibold mb-4">Recent Restores</h2>
            <div className="space-y-4">
              {restores.map(r => (
                <div key={r.id} className="flex justify-between items-center border-b pb-2">
                  <div>
                    <p className="font-medium text-gray-900 dark:text-white">Restore to {r.targetEnv}</p>
                    <p className="text-sm text-gray-500">PITR: {new Date(r.pointInTime).toLocaleString()} • {r.time}</p>
                  </div>
                  <span className="text-green-600 bg-green-100 px-2 py-1 rounded-full text-xs font-semibold">{r.status}</span>
                </div>
              ))}
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}
