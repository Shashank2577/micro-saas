"use client";

import { useEffect, useState } from "react";

export default function ScanJobs() {
  const [jobs, setJobs] = useState<{ [key: string]: string }[]>([]);

  useEffect(() => {
    // In a real application, you might have an endpoint to list all jobs, but for this exercise we fetch by repository ID or a general endpoint if it existed.
    // Adding mock for now until a general list scan jobs endpoint is added. We'll update the backend.
    fetch("/api/v1/licenseguard/scan-jobs", {
      headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" }
    })
      .then(res => res.json())
      .then(data => setJobs(data || []))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Recent Scan Jobs</h1>
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Job ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Repository ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Started At</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {jobs.map((job) => (
              <tr key={job.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{job.id}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{job.repositoryId}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                    {job.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {job.startedAt ? new Date(job.startedAt).toLocaleString() : 'N/A'}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
