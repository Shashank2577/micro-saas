"use client";
import React, { useEffect, useState } from 'react';

export default function SurveysPage() {
  const [surveys, setSurveys] = useState([]);

  useEffect(() => {
    fetch('/api/surveys', { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
      .then(res => res.json())
      .then(data => setSurveys(data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Pulse Surveys</h1>
      <div className="space-y-4">
        {surveys.map((s: any) => (
          <div key={s.id} className="p-4 bg-white shadow rounded flex justify-between items-center">
            <div>
              <h3 className="font-bold">{s.title}</h3>
              <p className="text-sm text-gray-500">Status: {s.status}</p>
            </div>
            <button className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">View</button>
          </div>
        ))}
        {surveys.length === 0 && <p>No surveys found.</p>}
      </div>
    </div>
  );
}
