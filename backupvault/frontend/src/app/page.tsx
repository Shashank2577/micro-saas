"use client";

import { useEffect, useState } from "react";
import { AlertTriangle, Database, Shield, History, Activity, AlertCircle } from "lucide-react";
import { AppSidebar } from "@/components/app-sidebar";

export default function BackupDashboard() {
  const [stats, setStats] = useState({
    totalPolicies: 0,
    recentExecutions: 0,
    successfulRestores: 0,
    rtoStatus: 'N/A'
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulated fetch
    setTimeout(() => {
      setStats({
        totalPolicies: 12,
        recentExecutions: 145,
        successfulRestores: 3,
        rtoStatus: '45m'
      });
      setLoading(false);
    }, 500);
  }, []);

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <AppSidebar />
      <div className="flex-1 overflow-auto">
        <header className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 px-8 py-6">
          <div className="flex justify-between items-center">
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Dashboard</h1>
          </div>
        </header>

        <main className="p-8 max-w-7xl mx-auto space-y-8">
          {/* Quick Stats */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            <StatCard
              icon={<Shield className="h-6 w-6 text-green-500" />}
              title="Active Policies"
              value={loading ? "-" : stats.totalPolicies}
            />
            <StatCard
              icon={<Activity className="h-6 w-6 text-blue-500" />}
              title="Recent Executions"
              value={loading ? "-" : stats.recentExecutions}
            />
            <StatCard
              icon={<History className="h-6 w-6 text-purple-500" />}
              title="Recent Restores"
              value={loading ? "-" : stats.successfulRestores}
            />
            <StatCard
              icon={<Database className="h-6 w-6 text-indigo-500" />}
              title="Last RTO Test"
              value={loading ? "-" : stats.rtoStatus}
            />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <div className="bg-white dark:bg-gray-800 p-6 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm">
               <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">Recent Executions</h2>
               <div className="space-y-4">
                  {loading ? (
                    <p className="text-gray-500 text-sm">Loading...</p>
                  ) : (
                    <>
                      <ExecutionRow name="Prod DB Backup" status="Success" time="2 hours ago" />
                      <ExecutionRow name="Staging DB Backup" status="Success" time="5 hours ago" />
                      <ExecutionRow name="User Data Backup" status="Failed" time="1 day ago" />
                    </>
                  )}
               </div>
            </div>

             <div className="bg-white dark:bg-gray-800 p-6 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm">
               <h2 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">System Alerts</h2>
               <div className="space-y-4">
                  <div className="flex items-start gap-3 p-3 bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 rounded-lg border border-red-100 dark:border-red-800">
                    <AlertCircle className="h-5 w-5 mt-0.5 shrink-0" />
                    <div>
                      <p className="font-medium text-sm">Backup Failed</p>
                      <p className="text-xs opacity-90">User Data Backup failed due to timeout. Retrying...</p>
                    </div>
                  </div>
                   <div className="flex items-start gap-3 p-3 bg-yellow-50 dark:bg-yellow-900/20 text-yellow-700 dark:text-yellow-400 rounded-lg border border-yellow-100 dark:border-yellow-800">
                    <AlertTriangle className="h-5 w-5 mt-0.5 shrink-0" />
                    <div>
                      <p className="font-medium text-sm">Storage Warning</p>
                      <p className="text-xs opacity-90">Archive storage at 85% capacity.</p>
                    </div>
                  </div>
               </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}

function StatCard({ icon, title, value }: { icon: React.ReactNode, title: string, value: string | number }) {
  return (
    <div className="bg-white dark:bg-gray-800 p-6 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm flex items-center gap-4">
      <div className="p-3 bg-gray-100 dark:bg-gray-700 rounded-lg">
        {icon}
      </div>
      <div>
        <p className="text-sm font-medium text-gray-500 dark:text-gray-400">{title}</p>
        <p className="text-2xl font-bold text-gray-900 dark:text-white">{value}</p>
      </div>
    </div>
  );
}

function ExecutionRow({ name, status, time }: { name: string, status: string, time: string }) {
  const isSuccess = status === "Success";
  return (
    <div className="flex justify-between items-center p-3 hover:bg-gray-50 dark:hover:bg-gray-700/50 rounded-lg transition-colors border border-gray-100 dark:border-gray-700">
      <div>
        <p className="font-medium text-sm text-gray-900 dark:text-white">{name}</p>
        <p className="text-xs text-gray-500">{time}</p>
      </div>
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${isSuccess ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400' : 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'}`}>
        {status}
      </span>
    </div>
  )
}
