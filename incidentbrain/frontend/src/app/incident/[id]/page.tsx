"use client";

import { useEffect, useState, use, useCallback } from "react";
import Link from "next/link";

interface TimelineEvent {
  timestamp: string;
  source: string;
  type: string;
  summary: string;
}

interface Incident {
  id: string;
  title: string;
  severity: string;
  status: string;
  rootCauseHypothesis?: string;
  confidenceScore?: number;
  postmortemDraft?: string;
  timelineEvents: TimelineEvent[];
}

export default function IncidentDetail({ params }: { params: Promise<{ id: string }> }) {
  const resolvedParams = use(params);
  const { id } = resolvedParams;
  const [incident, setIncident] = useState<Incident | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchIncident = useCallback(async () => {
    try {
      const res = await fetch(`/api/v1/incidents/${id}`);
      if (res.ok) {
        setIncident(await res.json());
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    fetchIncident();
  }, [fetchIncident]);

  const analyzeIncident = async () => {
    setLoading(true);
    try {
      await fetch(`/api/v1/incidents/${id}/analyze`, { method: "POST" });
      await fetchIncident();
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const generatePostmortem = async () => {
    setLoading(true);
    try {
      await fetch(`/api/v1/incidents/${id}/postmortem`, { method: "POST" });
      await fetchIncident();
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const ingestLogs = async () => {
    setLoading(true);
    try {
      await fetch(`/api/v1/incidents/${id}/datadog/logs?service=my-service`, { method: "POST" });
      await fetchIncident();
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  if (loading && !incident) return <div className="p-8">Loading...</div>;
  if (!incident) return <div className="p-8">Incident not found.</div>;

  return (
    <main className="min-h-screen p-8 bg-gray-50 text-gray-900 font-sans">
      <div className="max-w-4xl mx-auto">
        <Link href="/" className="text-blue-600 hover:underline mb-4 inline-block">&larr; Back to Dashboard</Link>
        
        <div className="bg-white p-6 rounded-lg shadow mb-8">
          <div className="flex justify-between items-start">
            <div>
              <h1 className="text-3xl font-bold mb-2">{incident.title}</h1>
              <p className="text-gray-600">ID: {incident.id}</p>
              <p className="text-gray-600">Severity: {incident.severity} | Status: {incident.status}</p>
            </div>
            <div className="flex gap-2">
               <button 
                className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 font-medium"
                onClick={ingestLogs}
                disabled={loading}
              >
                Ingest Logs
              </button>
              <button 
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 font-medium"
                onClick={analyzeIncident}
                disabled={loading}
              >
                Run AI Analysis
              </button>
              <button 
                className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 font-medium"
                onClick={generatePostmortem}
                disabled={loading}
              >
                Generate Postmortem
              </button>
            </div>
          </div>
        </div>

        {incident.rootCauseHypothesis && (
          <div className="bg-white p-6 rounded-lg shadow mb-8 border-l-4 border-blue-500">
            <h2 className="text-xl font-semibold mb-4">AI Root Cause Hypothesis</h2>
            <p className="mb-4 whitespace-pre-wrap">{incident.rootCauseHypothesis}</p>
            <p className="text-sm font-medium text-gray-500">
              Confidence Score: {incident.confidenceScore ? (incident.confidenceScore * 100).toFixed(0) + '%' : 'N/A'}
            </p>
          </div>
        )}

        {incident.postmortemDraft && (
          <div className="bg-white p-6 rounded-lg shadow mb-8 border-l-4 border-green-500">
            <h2 className="text-xl font-semibold mb-4">Postmortem Draft</h2>
            <div className="prose max-w-none whitespace-pre-wrap">
              {incident.postmortemDraft}
            </div>
          </div>
        )}

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">Incident Timeline</h2>
          {(!incident.timelineEvents || incident.timelineEvents.length === 0) ? (
            <p className="text-gray-500">No events in timeline.</p>
          ) : (
            <div className="space-y-4">
              {incident.timelineEvents.map((event, idx) => (
                <div key={idx} className="flex gap-4 items-start">
                  <div className="text-sm text-gray-500 whitespace-nowrap pt-1">
                    {new Date(event.timestamp).toLocaleTimeString()}
                  </div>
                  <div className="border-l-2 pl-4 pb-4">
                    <span className="inline-block bg-gray-100 text-xs px-2 py-1 rounded font-medium mb-1 border">
                      {event.source} - {event.type}
                    </span>
                    <p className="text-gray-800">{event.summary}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </main>
  );
}
