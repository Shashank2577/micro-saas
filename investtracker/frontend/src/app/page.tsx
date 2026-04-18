"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import Link from "next/link";

interface Portfolio {
  id: string;
  name: string;
  currency: string;
  targetAllocation: string;
}

export default function Home() {
  const [portfolios, setPortfolios] = useState<Portfolio[]>([]);

  useEffect(() => {
    async function loadPortfolios() {
      try {
        const response = await api.get("/api/v1/portfolios");
        setPortfolios(response.data);
      } catch (error) {
        console.error("Failed to load portfolios", error);
      }
    }
    loadPortfolios();
  }, []);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">InvestTracker</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {portfolios.map((portfolio) => (
          <div key={portfolio.id} className="border p-4 rounded-lg shadow">
            <h2 className="text-xl font-semibold">{portfolio.name}</h2>
            <p>Currency: {portfolio.currency}</p>
            <Link href={`/portfolios/${portfolio.id}`} className="text-blue-500 hover:underline">
              View Dashboard
            </Link>
          </div>
        ))}
      </div>
      {portfolios.length === 0 && (
        <p>No portfolios found. Create one to get started.</p>
      )}
    </div>
  );
}
