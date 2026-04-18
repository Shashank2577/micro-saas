"use client";

import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useParams } from "next/navigation";

interface Portfolio {
  id: string;
  name: string;
  currency: string;
  targetAllocation: string;
}

interface AiInsight {
  riskScore: number;
  volatilityAssessment: string;
  rebalanceRecommendations: string[];
}

export default function PortfolioDashboard() {
  const params = useParams();
  const id = params.id as string;
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null);
  const [insight, setInsight] = useState<AiInsight | null>(null);

  useEffect(() => {
    async function loadData() {
      try {
        const portRes = await api.get(`/api/v1/portfolios/${id}`);
        setPortfolio(portRes.data);
        const aiRes = await api.get(`/api/v1/portfolios/${id}/ai-insights`);
        setInsight(aiRes.data);
      } catch (error) {
        console.error("Failed to load portfolio data", error);
      }
    }
    loadData();
  }, [id]);

  if (!portfolio) return <div>Loading...</div>;

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-4">{portfolio.name} Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="border p-4 rounded-lg shadow bg-white">
          <h2 className="text-xl font-semibold mb-2">Portfolio Details</h2>
          <p><strong>Currency:</strong> {portfolio.currency}</p>
          <p><strong>Target Allocation:</strong> {portfolio.targetAllocation || "Not set"}</p>
        </div>
        
        {insight && (
          <div className="border p-4 rounded-lg shadow bg-blue-50">
            <h2 className="text-xl font-semibold mb-2 text-blue-800">AI Risk & Insights</h2>
            <p><strong>Risk Score:</strong> {insight.riskScore}/100</p>
            <p><strong>Volatility:</strong> {insight.volatilityAssessment}</p>
            <h3 className="font-semibold mt-2">Recommendations:</h3>
            <ul className="list-disc ml-5">
              {insight.rebalanceRecommendations.map((rec, idx) => (
                <li key={idx}>{rec}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}
