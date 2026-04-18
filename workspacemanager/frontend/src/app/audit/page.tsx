"use client";

import { useState, useEffect } from 'react';
import api from '../../lib/api';

export default function AuditLogs() {
  const [logs, setLogs] = useState<any[]>([]);

  useEffect(() => {
    fetchLogs();
  }, []);

  const fetchLogs = async () => {
    try {
      const { data } = await api.get('/api/v1/audit-logs');
      setLogs(data);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold text-gray-900">Audit Logs</h1>
      <div className="mt-8 flex flex-col">
        <div className="-my-2 -mx-4 overflow-x-auto sm:-mx-6 lg:-mx-8">
          <div className="inline-block min-w-full py-2 align-middle md:px-6 lg:px-8">
            <div className="overflow-hidden shadow ring-1 ring-black ring-opacity-5 md:rounded-lg">
              <table className="min-w-full divide-y divide-gray-300">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">Action</th>
                    <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Actor ID</th>
                    <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Target ID</th>
                    <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Time</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200 bg-white">
                  {logs.map((log) => (
                    <tr key={log.id}>
                      <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">{log.action}</td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{log.actorId || 'System'}</td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{log.targetId}</td>
                      <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{new Date(log.createdAt).toLocaleString()}</td>
                    </tr>
                  ))}
                  {logs.length === 0 && (
                    <tr>
                      <td colSpan={4} className="whitespace-nowrap py-4 text-center text-sm text-gray-500">No logs found</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
