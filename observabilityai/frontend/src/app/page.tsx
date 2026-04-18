"use client";

import { useEffect, useState } from "react";
import api from "@/lib/api";

export default function Home() {
  const [signals, setSignals] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [signalsRes, alertsRes] = await Promise.all([
          api.get("/api/signals"),
          api.get("/api/alerts"),
        ]);
        setSignals(signalsRes.data);
        setAlerts(alertsRes.data);
      } catch (err) {
        console.error("Failed to load dashboard data", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">ObservabilityAI Dashboard</h1>

      {loading ? (
        <p>Loading signals and alerts...</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div className="border p-6 rounded shadow-sm">
            <h2 className="text-xl font-semibold mb-4">Recent Signals</h2>
            {signals.length === 0 ? (
              <p>No signals available.</p>
            ) : (
              <ul>
                {signals.slice(0, 5).map((s: any) => (
                  <li key={s.id} className="mb-2 border-b pb-2">
                    <span className="font-bold">[{s.signalType}]</span> {s.serviceName} - {s.timestamp}
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="border p-6 rounded shadow-sm">
            <h2 className="text-xl font-semibold mb-4">Active Alerts</h2>
            {alerts.length === 0 ? (
              <p>No alerts active.</p>
            ) : (
              <ul>
                {alerts.map((a: any) => (
                  <li key={a.id} className="mb-2 border-b pb-2">
                    <span className="font-bold text-red-500">[{a.severity}]</span> {a.title}
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
