import React from "react";

export function Dashboard({ anomalies, reviews }: { anomalies: any[], reviews: any[] }) {
  return (
    <div className="grid grid-cols-2 gap-4">
      <div className="border p-4 rounded shadow">
        <h2 className="text-xl font-semibold mb-2">Open Anomalies</h2>
        <ul>
          {anomalies.map((a: any) => (
            <li key={a.id} className="mb-2">
              <span className="font-bold text-red-500">{a.severity}</span>: {a.anomalyType} ({a.status})
            </li>
          ))}
        </ul>
      </div>
      <div className="border p-4 rounded shadow">
        <h2 className="text-xl font-semibold mb-2">Pending Reviews</h2>
        <ul>
          {reviews.map((r: any) => (
            <li key={r.id} className="mb-2">
               {r.status} - AI says: {r.aiRecommendation}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
