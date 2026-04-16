"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

interface Incident {
  id: string;
  title: string;
  severity: string;
  status: string;
}

export default function Home() {
  const [incidents, setIncidents] = useState<Incident[]>([]);
  const [newTitle, setNewTitle] = useState("");
  const [newSeverity, setNewSeverity] = useState("HIGH");

  useEffect(() => {
    const fetchIncidents = async () => {
      try {
        const res = await fetch("/api/v1/incidents");
        if (res.ok) {
          const data = await res.json();
          // Since we didn't implement a GET /api/v1/incidents to list all, let's just keep this as a placeholder or mock
          // For MVP, we might just be showing a few static ones or let the user create one
          setIncidents(data);
        }
      } catch (e) {
        console.error(e);
      }
    };

    fetchIncidents();
  }, []);

  const createIncident = async () => {
    try {
      const res = await fetch("/api/v1/incidents", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title: newTitle, severity: newSeverity }),
      });
      if (res.ok) {
        const data = await res.json();
        setIncidents([...incidents, data]);
        setNewTitle("");
      }
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <main className="min-h-screen p-8 bg-gray-50 text-gray-900 font-sans">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold mb-8">IncidentBrain Dashboard</h1>
        
        <div className="bg-white p-6 rounded-lg shadow mb-8">
          <h2 className="text-xl font-semibold mb-4">Create Incident</h2>
          <div className="flex gap-4">
            <input 
              type="text" 
              className="border p-2 rounded flex-1" 
              placeholder="Incident Title..." 
              value={newTitle}
              onChange={(e) => setNewTitle(e.target.value)}
            />
            <select 
              className="border p-2 rounded"
              value={newSeverity}
              onChange={(e) => setNewSeverity(e.target.value)}
            >
              <option value="CRITICAL">CRITICAL</option>
              <option value="HIGH">HIGH</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="LOW">LOW</option>
            </select>
            <button 
              className="bg-blue-600 text-white px-4 py-2 rounded font-medium hover:bg-blue-700"
              onClick={createIncident}
            >
              Create
            </button>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">Recent Incidents</h2>
          {incidents.length === 0 ? (
            <p className="text-gray-500">No incidents found. Create one above.</p>
          ) : (
            <div className="space-y-4">
              {incidents.map(inc => (
                <div key={inc.id} className="border p-4 rounded flex justify-between items-center">
                  <div>
                    <h3 className="font-semibold text-lg">{inc.title}</h3>
                    <p className="text-sm text-gray-600">Severity: {inc.severity} | Status: {inc.status}</p>
                  </div>
                  <Link href={`/incident/${inc.id}`} className="text-blue-600 hover:underline">
                    View Details
                  </Link>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </main>
  );
}
