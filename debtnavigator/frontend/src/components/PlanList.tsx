"use client";

import React, { useEffect, useState } from "react";
import { apiFetch } from "@/lib/api";

export default function PlanList() {
  const [plans, setPlans] = useState<any[]>([]);

  useEffect(() => {
    apiFetch("/api/v1/debt/payment-plans").then((data) => {
      setPlans(data || []);
    });
  }, []);

  return (
    <div>
      {plans.length === 0 ? (
        <p>No plans found.</p>
      ) : (
        <ul data-testid="plan-list">
          {plans.map((p: any) => (
            <li key={p.id} className="border p-4 mb-2 rounded">
              {p.name} - {p.status}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
