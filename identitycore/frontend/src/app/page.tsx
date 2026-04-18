"use client";

import { useEffect, useState } from "react";
import { Dashboard } from "@/components/Dashboard";

export default function DashboardPage() {
  const [anomalies, setAnomalies] = useState<any[]>([]);
  const [reviews, setReviews] = useState<any[]>([]);

  useEffect(() => {
    // Placeholder fetching logic
    setAnomalies([
      { id: "1", anomalyType: "UNUSUAL_TIME", severity: "HIGH", status: "OPEN" },
    ]);
    setReviews([
      { id: "1", status: "PENDING", aiRecommendation: "REVOKE all" },
    ]);
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">IdentityCore Dashboard</h1>
      <Dashboard anomalies={anomalies} reviews={reviews} />
    </div>
  );
}
