"use client";

import React, { useEffect, useState } from "react";
import { apiFetch } from "@/lib/api";

export default function RiskProjectionList() {
  const [projections, setProjections] = useState<any[]>([]);

  useEffect(() => {
    apiFetch("/api/v1/debt/risk-projections").then((data) => {
      setProjections(data || []);
    });
  }, []);

  return (
    <div>
      {projections.length === 0 ? (
        <p>No projections found.</p>
      ) : (
        <ul data-testid="projection-list">
          {projections.map((p: any) => (
            <li key={p.id} className="border p-4 mb-2 rounded">
              {p.name} - {p.status}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
