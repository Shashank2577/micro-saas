"use client";

import React, { useEffect, useState } from 'react';
import api from '../../lib/api';
import { Play, RotateCcw, XCircle } from 'lucide-react';
import { formatDistanceToNow } from 'date-fns';

export default function JobsPage() {
  const [jobs, setJobs] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [statusFilter, setStatusFilter] = useState('');

  const fetchJobs = async () => {
    setLoading(true);
    try {
      const url = statusFilter ? `/jobs?status=${statusFilter}` : '/jobs';
      const { data } = await api.get(url);
      setJobs(data);
    } catch (err) {
      console.error(err);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchJobs();
  }, [statusFilter]);

  const enqueueTestJob = async (name: string, priority = 'NORMAL') => {
    try {
      await api.post('/jobs', { name, priority, payload: '{"test":"true"}' });
      fetchJobs();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold">Jobs</h2>
        <div className="flex space-x-2">
          <button onClick={() => enqueueTestJob('send_email', 'HIGH')} className="bg-blue-600 text-white px-4 py-2 rounded shadow hover:bg-blue-700">Queue Email (High)</button>
          <button onClick={() => enqueueTestJob('fail_test')} className="bg-red-600 text-white px-4 py-2 rounded shadow hover:bg-red-700">Queue Fail Test</button>
        </div>
      </div>

      <div className="bg-white shadow rounded-lg overflow-hidden">
        <div className="p-4 border-b">
          <select value={statusFilter} onChange={e => setStatusFilter(e.target.value)} className="border rounded p-2">
            <option value="">All Statuses</option>
            <option value="PENDING">Pending</option>
            <option value="RUNNING">Running</option>
            <option value="COMPLETED">Completed</option>
            <option value="FAILED">Failed</option>
            <option value="DEAD_LETTER">Dead Letter</option>
          </select>
        </div>
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID / Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Priority</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Retries</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {jobs.map(job => (
              <tr key={job.id}>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm font-medium text-gray-900">{job.name}</div>
                  <div className="text-xs text-gray-500">{job.id.substring(0, 8)}...</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                    ${job.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 
                      job.status === 'FAILED' ? 'bg-red-100 text-red-800' :
                      job.status === 'DEAD_LETTER' ? 'bg-gray-100 text-gray-800' :
                      job.status === 'RUNNING' ? 'bg-blue-100 text-blue-800' :
                      'bg-yellow-100 text-yellow-800'}`}>
                    {job.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{job.priority}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{job.retryCount} / {job.maxRetries}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {formatDistanceToNow(new Date(job.createdAt), { addSuffix: true })}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  {(job.status === 'FAILED' || job.status === 'DEAD_LETTER') && (
                    <button onClick={async () => { await api.post(`/jobs/${job.id}/retry`); fetchJobs(); }} className="text-indigo-600 hover:text-indigo-900 mr-3">
                      <RotateCcw className="w-4 h-4 inline" /> Retry
                    </button>
                  )}
                  {(job.status === 'PENDING' || job.status === 'RUNNING') && (
                    <button onClick={async () => { await api.post(`/jobs/${job.id}/cancel`); fetchJobs(); }} className="text-red-600 hover:text-red-900">
                      <XCircle className="w-4 h-4 inline" /> Cancel
                    </button>
                  )}
                </td>
              </tr>
            ))}
            {jobs.length === 0 && !loading && (
              <tr>
                <td colSpan={6} className="px-6 py-4 text-center text-gray-500">No jobs found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
