"use client";

import React, { useEffect, useState } from "react";
import { apiFetch } from "@/lib/api";

export default function OptimizationRunList() {
  const [runs, setRuns] = useState<any[]>([]);

  useEffect(() => {
    apiFetch("/api/v1/debt/optimization-runs").then((data) => {
      setRuns(data || []);
    });
  }, []);

  return (
    <div>
      {runs.length === 0 ? (
        <p>No runs found.</p>
      ) : (
        <ul data-testid="optimization-list">
          {runs.map((r: any) => (
            <li key={r.id} className="border p-4 mb-2 rounded">
              {r.name} - {r.status}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
