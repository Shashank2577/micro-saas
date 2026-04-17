'use client';
import { api } from '@/lib/api';
import { useQuery } from '@tanstack/react-query';

export default function AuditPage() {
  const { data: logs = [], isLoading } = useQuery({
    queryKey: ['audit'],
    queryFn: api.audit.list,
  });

  return (
    <div className="p-8 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Audit Logs</h1>
      
      <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4 text-left">Timestamp</th>
              <th className="p-4 text-left">Actor</th>
              <th className="p-4 text-left">Action</th>
              <th className="p-4 text-left">Resource</th>
              <th className="p-4 text-left">Details</th>
            </tr>
          </thead>
          <tbody>
            {isLoading && <tr><td colSpan={5} className="p-4 text-center">Loading...</td></tr>}
            {logs.map(log => (
              <tr key={log.id} className="border-b text-sm">
                <td className="p-4 whitespace-nowrap text-gray-500">{new Date(log.timestamp).toLocaleString()}</td>
                <td className="p-4 font-medium">{log.actor}</td>
                <td className="p-4">
                  <span className="bg-gray-100 px-2 py-1 rounded text-gray-800">{log.action}</span>
                </td>
                <td className="p-4 text-gray-600">{log.resource}</td>
                <td className="p-4 text-gray-500 truncate max-w-xs">{log.details}</td>
              </tr>
            ))}
            {!isLoading && logs.length === 0 && (
              <tr><td colSpan={5} className="p-4 text-center text-gray-500">No audit logs found.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
