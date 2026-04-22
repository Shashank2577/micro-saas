"use client";

import { useEffect, useState } from "react";

export default function Repositories() {
  const [repos, setRepos] = useState<{ [key: string]: string }[]>([]);

  useEffect(() => {
    fetch("/api/v1/licenseguard/repositories", {
      headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" }
    })
      .then(res => res.json())
      .then(data => setRepos(data || []))
      .catch(err => console.error(err));
  }, []);

  const handleScan = (repoId: string) => {
    fetch(`/api/v1/licenseguard/scan/${repoId}`, {
      method: "POST",
      headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" }
    })
      .then(() => alert("Scan initiated!"))
      .catch(err => console.error(err));
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Repositories</h1>
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">URL</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Last Scanned</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {repos.map((repo) => (
              <tr key={repo.id}>
                <td className="px-6 py-4 whitespace-nowrap">{repo.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-blue-600">{repo.repoUrl}</td>
                <td className="px-6 py-4 whitespace-nowrap">{repo.lastScannedAt ? new Date(repo.lastScannedAt).toLocaleDateString() : 'Never'}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <button onClick={() => handleScan(repo.id)} className="text-indigo-600 hover:text-indigo-900">Scan Now</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
