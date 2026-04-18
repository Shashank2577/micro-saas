"use client";

import { useEffect, useState } from 'react';
import { api, Integration, SyncJob, AuditLog } from '@/lib/api';
import Link from 'next/link';

export default function IntegrationDetailsPage({ params }: { params: { id: string } }) {
  const [integration, setIntegration] = useState<Integration | null>(null);
  const [syncJobs, setSyncJobs] = useState<SyncJob[]>([]);
  const [auditLogs, setAuditLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.integrations.get(params.id),
      api.integrations.syncJobs(params.id),
      api.integrations.auditLogs(params.id)
    ])
    .then(([i, s, a]) => {
      setIntegration(i);
      setSyncJobs(s);
      setAuditLogs(a);
    })
    .catch(console.error)
    .finally(() => setLoading(false));
  }, [params.id]);

  if (loading) return <div className="p-8 text-center">Loading...</div>;
  if (!integration) return <div className="p-8 text-center text-red-600">Integration not found</div>;

  return (
    <main className="min-h-screen p-8 bg-gray-50">
      <div className="max-w-6xl mx-auto space-y-8">
        <header className="flex items-center gap-4">
          <Link href="/" className="text-gray-500 hover:text-gray-900">&larr; Back</Link>
          <h1 className="text-3xl font-bold text-gray-900">{integration.provider} Integration</h1>
          <span className={`px-3 py-1 text-sm font-medium rounded-full ${
            integration.status === 'HEALTHY' ? 'bg-green-100 text-green-800' :
            integration.status === 'ERROR' ? 'bg-red-100 text-red-800' :
            'bg-yellow-100 text-yellow-800'
          }`}>
            {integration.status}
          </span>
        </header>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Main Content */}
          <div className="lg:col-span-2 space-y-8">
            
            {/* Sync Jobs */}
            <section className="bg-white p-6 rounded-lg shadow">
              <div className="flex justify-between items-center mb-6">
                <h2 className="text-xl font-semibold">Sync Jobs</h2>
                <button className="text-sm bg-gray-100 hover:bg-gray-200 px-3 py-1 rounded">
                  + New Job
                </button>
              </div>
              
              {syncJobs.length === 0 ? (
                <p className="text-gray-500 text-sm">No sync jobs configured.</p>
              ) : (
                <div className="space-y-4">
                  {syncJobs.map(job => (
                    <div key={job.id} className="border border-gray-200 rounded p-4 flex justify-between items-center">
                      <div>
                        <p className="font-medium">{job.sourceEntity} &rarr; {job.targetEntity}</p>
                        <p className="text-xs text-gray-500 mt-1">Schedule: {job.scheduleCron}</p>
                      </div>
                      <div className="text-right">
                        <span className="text-xs bg-blue-50 text-blue-700 px-2 py-1 rounded">
                          {job.status}
                        </span>
                        {job.lastRunAt && <p className="text-xs text-gray-500 mt-2">Last run: {new Date(job.lastRunAt).toLocaleString()}</p>}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </section>

            {/* Audit Logs */}
            <section className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-semibold mb-6">Recent Activity</h2>
              {auditLogs.length === 0 ? (
                <p className="text-gray-500 text-sm">No activity recorded yet.</p>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm text-left">
                    <thead className="text-xs text-gray-500 uppercase bg-gray-50">
                      <tr>
                        <th className="px-4 py-2">Time</th>
                        <th className="px-4 py-2">Action</th>
                        <th className="px-4 py-2">Status</th>
                        <th className="px-4 py-2">Records</th>
                      </tr>
                    </thead>
                    <tbody>
                      {auditLogs.map(log => (
                        <tr key={log.id} className="border-b">
                          <td className="px-4 py-3 whitespace-nowrap">{new Date(log.createdAt).toLocaleString()}</td>
                          <td className="px-4 py-3 font-medium text-gray-900">{log.action}</td>
                          <td className="px-4 py-3">
                            <span className={log.status === 'SUCCESS' ? 'text-green-600' : 'text-red-600'}>
                              {log.status}
                            </span>
                          </td>
                          <td className="px-4 py-3">{log.recordsProcessed}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </section>

          </div>

          {/* Sidebar */}
          <div className="space-y-8">
            {/* Credentials */}
            <section className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-semibold mb-4">Credentials</h2>
              <div className="space-y-4">
                <p className="text-sm text-gray-600">
                  Authentication is configured using <strong>{integration.authType}</strong>.
                </p>
                {integration.status === 'PENDING' && integration.authType === 'OAUTH2' && (
                  <button className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition text-sm">
                    Connect via OAuth
                  </button>
                )}
                <button className="w-full bg-white border border-gray-300 text-gray-700 py-2 rounded hover:bg-gray-50 transition text-sm">
                  Test Connection
                </button>
              </div>
            </section>
          </div>

        </div>
      </div>
    </main>
  );
}
