"use client";

import React, { useEffect, useState } from "react";
import { apiFetch } from "@/lib/api";

export default function ConsolidationOfferList() {
  const [offers, setOffers] = useState<any[]>([]);

  useEffect(() => {
    apiFetch("/api/v1/debt/consolidation-offers").then((data) => {
      setOffers(data || []);
    });
  }, []);

  return (
    <div>
      {offers.length === 0 ? (
        <p>No offers found.</p>
      ) : (
        <ul data-testid="consolidation-list">
          {offers.map((o: any) => (
            <li key={o.id} className="border p-4 mb-2 rounded">
              {o.name} - {o.status}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
