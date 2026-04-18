"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

interface Client {
  id: string;
  name: string;
  email: string;
  phone: string;
}

export default function ClientsPage() {
  const [clients, setClients] = useState<Client[]>([]);

  useEffect(() => {
    // In a real app, you'd fetch with X-Tenant-ID header
    fetch("/api/clients", { headers: { "X-Tenant-ID": "tenant-1" } })
      .then((res) => {
        if (!res.ok) {
           return []; // fallback for dev
        }
        return res.json();
      })
      .then((data) => setClients(data))
      .catch(console.error);
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Clients</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {clients.length === 0 ? (
           <p>No clients found. Or API is not reachable.</p>
        ) : (
          clients.map((client) => (
            <Link key={client.id} href={`/clients/${client.id}`}>
              <div className="border p-4 rounded shadow hover:shadow-md transition">
                <h2 className="text-xl font-semibold">{client.name}</h2>
                <p className="text-gray-600">{client.email}</p>
              </div>
            </Link>
          ))
        )}
      </div>
    </div>
  );
}
