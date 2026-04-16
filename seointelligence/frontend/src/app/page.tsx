"use client";

import { useEffect, useState } from "react";
import { PlusIcon } from "lucide-react";

type Domain = {
  id: string;
  url: string;
  name: string;
  trackedSince: string;
  overallAuthorityScore: number;
};

export default function Home() {
  const [domains, setDomains] = useState<Domain[]>([]);
  const [newDomainUrl, setNewDomainUrl] = useState("");
  const [newDomainName, setNewDomainName] = useState("");

  const fetchDomains = async () => {
    try {
      const res = await fetch("http://localhost:8133/api/v1/domains", {
        headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" },
      });
      if (res.ok) {
        setDomains(await res.json());
      }
    } catch (error) {
      console.error("Failed to fetch domains", error);
    }
  };

  useEffect(() => {
    fetchDomains();
  }, []);

  const addDomain = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newDomainUrl || !newDomainName) return;

    try {
      const res = await fetch("http://localhost:8133/api/v1/domains", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Tenant-ID": "00000000-0000-0000-0000-000000000001",
        },
        body: JSON.stringify({ url: newDomainUrl, name: newDomainName }),
      });
      if (res.ok) {
        setNewDomainUrl("");
        setNewDomainName("");
        fetchDomains();
      }
    } catch (error) {
      console.error("Failed to add domain", error);
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">SEO Intelligence</h1>
      </div>

      <div className="bg-white p-6 rounded-lg shadow mb-8">
        <h2 className="text-xl font-semibold mb-4">Add Tracked Domain</h2>
        <form onSubmit={addDomain} className="flex gap-4 items-end">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
            <input
              type="text"
              className="w-full border border-gray-300 rounded-md p-2 text-gray-900"
              value={newDomainName}
              onChange={(e) => setNewDomainName(e.target.value)}
              placeholder="e.g., NexusHub"
              required
            />
          </div>
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">URL</label>
            <input
              type="url"
              className="w-full border border-gray-300 rounded-md p-2 text-gray-900"
              value={newDomainUrl}
              onChange={(e) => setNewDomainUrl(e.target.value)}
              placeholder="https://example.com"
              required
            />
          </div>
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 flex items-center gap-2"
          >
            <PlusIcon className="w-5 h-5" />
            Add Domain
          </button>
        </form>
      </div>

      <div className="grid grid-cols-1 gap-6">
        {domains.map((domain) => (
          <div key={domain.id} className="bg-white p-6 rounded-lg shadow border border-gray-100 flex justify-between items-center">
            <div>
              <h3 className="text-xl font-bold text-gray-900">{domain.name}</h3>
              <p className="text-gray-500">{domain.url}</p>
              <div className="mt-2 text-sm text-gray-600">
                Tracked since: {domain.trackedSince} | Auth Score: {domain.overallAuthorityScore}
              </div>
            </div>
            <a href={`/domain/${domain.id}`} className="text-blue-600 hover:text-blue-800 font-medium">
              View Details &rarr;
            </a>
          </div>
        ))}
        {domains.length === 0 && (
          <div className="text-center text-gray-500 py-12 bg-white rounded-lg shadow">
            No domains tracked yet. Add one above to get started.
          </div>
        )}
      </div>
    </div>
  );
}
