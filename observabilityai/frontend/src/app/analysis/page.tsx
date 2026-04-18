"use client";

import { useState } from "react";
import api from "@/lib/api";

export default function AnalysisPage() {
  const [traceId, setTraceId] = useState("");
  const [analysis, setAnalysis] = useState<any>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleAnalyze = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setAnalysis(null);

    try {
      const res = await api.post("/api/analyze", { traceId });
      setAnalysis(res.data);
    } catch (err: any) {
      if (err.response?.status === 404) {
        setError("Trace ID not found.");
      } else {
        setError("Failed to run analysis.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">AI Root Cause Analysis</h1>
      
      <form onSubmit={handleAnalyze} className="mb-8 flex gap-4">
        <input 
          type="text" 
          value={traceId} 
          onChange={(e) => setTraceId(e.target.value)} 
          placeholder="Enter Trace ID..." 
          className="border p-2 rounded flex-grow text-black"
          required
        />
        <button 
          type="submit" 
          className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
          disabled={loading}
        >
          {loading ? "Analyzing..." : "Analyze Trace"}
        </button>
      </form>

      {error && <p className="text-red-500 mb-4">{error}</p>}

      {analysis && (
        <div className="border p-6 rounded bg-gray-50 shadow-sm text-black">
          <h2 className="text-xl font-semibold mb-4">Analysis Result</h2>
          {typeof analysis === 'string' ? (
             <p>{analysis}</p>
          ) : (
            <pre className="whitespace-pre-wrap">{JSON.stringify(analysis, null, 2)}</pre>
          )}
        </div>
      )}
    </div>
  );
}
