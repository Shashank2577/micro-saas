"use client";

import { useEffect, useState } from "react";

export default function Policies() {
  const [policies, setPolicies] = useState<{ [key: string]: string }[]>([]);

  useEffect(() => {
    fetch("/api/v1/licenseguard/policies", {
      headers: { "X-Tenant-ID": "00000000-0000-0000-0000-000000000001" }
    })
      .then(res => res.json())
      .then(data => setPolicies(data || []))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Compliance Policies</h1>
      <div className="grid gap-4">
        {policies.map(p => (
          <div key={p.id} className="p-6 bg-white rounded shadow">
            <h2 className="text-xl font-semibold">{p.name}</h2>
            <div className="mt-4 grid grid-cols-2 gap-4 text-sm">
              <div>
                <strong>Allowed:</strong> {p.allowedLicensesJson}
              </div>
              <div>
                <strong>Denied:</strong> {p.deniedLicensesJson}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
