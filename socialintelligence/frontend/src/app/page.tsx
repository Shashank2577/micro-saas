"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

interface Profile {
  id: string;
  platform: string;
  handle: string;
  displayName: string;
  followerCount: number;
}

interface Mention {
  id: string;
  platform: string;
  content: string;
  sentiment: string;
  detectedAt: string;
}

interface Trend {
  id: string;
  topic: string;
  signalStrength: number;
  status: string;
}

export default function Home() {
  const [profiles, setProfiles] = useState<Profile[]>([]);
  const [mentions, setMentions] = useState<Mention[]>([]);
  const [trends, setTrends] = useState<Trend[]>([]);
  const [activeTab, setActiveTab] = useState("profiles");

  useEffect(() => {
    fetch("/api/v1/profiles", { headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000000" } })
      .then((res) => res.json())
      .then((data) => setProfiles(data))
      .catch(console.error);

    fetch("/api/v1/mentions", { headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000000" } })
      .then((res) => res.json())
      .then((data) => setMentions(data))
      .catch(console.error);

    fetch("/api/v1/trends", { headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000000" } })
      .then((res) => res.json())
      .then((data) => setTrends(data))
      .catch(console.error);
  }, []);

  return (
    <main className="min-h-screen p-8 bg-gray-50 text-gray-900 font-sans">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-3xl font-bold mb-8">Social Intelligence Dashboard</h1>

        <div className="flex gap-4 mb-8">
          <button
            onClick={() => setActiveTab("profiles")}
            className={`px-4 py-2 rounded ${activeTab === "profiles" ? "bg-blue-600 text-white" : "bg-white border"}`}
          >
            Profiles
          </button>
          <button
            onClick={() => setActiveTab("mentions")}
            className={`px-4 py-2 rounded ${activeTab === "mentions" ? "bg-blue-600 text-white" : "bg-white border"}`}
          >
            Mentions
          </button>
          <button
            onClick={() => setActiveTab("trends")}
            className={`px-4 py-2 rounded ${activeTab === "trends" ? "bg-blue-600 text-white" : "bg-white border"}`}
          >
            Trends
          </button>
        </div>

        {activeTab === "profiles" && (
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Tracked Profiles</h2>
            {profiles.length === 0 ? (
              <p className="text-gray-500">No profiles tracked yet.</p>
            ) : (
              <div className="grid gap-4">
                {profiles.map((p) => (
                  <div key={p.id} className="border p-4 rounded flex justify-between">
                    <div>
                      <h3 className="font-semibold text-lg">{p.displayName}</h3>
                      <p className="text-sm text-gray-600">
                        {p.platform} - {p.handle}
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="font-bold">{p.followerCount}</p>
                      <p className="text-sm text-gray-500">Followers</p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === "mentions" && (
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Recent Mentions</h2>
            {mentions.length === 0 ? (
              <p className="text-gray-500">No mentions found.</p>
            ) : (
              <div className="grid gap-4">
                {mentions.map((m) => (
                  <div key={m.id} className="border p-4 rounded">
                    <div className="flex justify-between mb-2">
                      <span className="text-sm font-medium px-2 py-1 bg-gray-100 rounded">{m.platform}</span>
                      <span className={`text-sm font-medium px-2 py-1 rounded ${m.sentiment === "POSITIVE" ? "bg-green-100 text-green-800" : m.sentiment === "NEGATIVE" ? "bg-red-100 text-red-800" : "bg-gray-100"}`}>
                        {m.sentiment}
                      </span>
                    </div>
                    <p className="text-gray-800">{m.content}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === "trends" && (
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Emerging Trends</h2>
            {trends.length === 0 ? (
              <p className="text-gray-500">No emerging trends detected.</p>
            ) : (
              <div className="grid gap-4">
                {trends.map((t) => (
                  <div key={t.id} className="border p-4 rounded flex justify-between items-center">
                    <div>
                      <h3 className="font-semibold text-lg">{t.topic}</h3>
                      <p className="text-sm text-gray-600">Signal Strength: {t.signalStrength}</p>
                    </div>
                    <span className="text-sm font-medium px-2 py-1 bg-purple-100 text-purple-800 rounded">
                      {t.status}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </main>
  );
}
