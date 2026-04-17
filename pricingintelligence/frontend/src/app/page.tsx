"use client";
import React, { useEffect, useState } from "react";

interface ElasticityModel {
  id: string;
  segment: string;
  basePrice: number;
  elasticityCoefficient: number;
  confidenceScore: number;
}

interface PricingExperiment {
  id: string;
  name: string;
  segment: string;
  controlPrice: number;
  variantPrice: number;
  status: string;
}

export default function Home() {
  const [models, setModels] = useState<ElasticityModel[]>([]);
  const [experiments, setExperiments] = useState<PricingExperiment[]>([]);

  useEffect(() => {
    // Fetching from backend
    const fetchModels = async () => {
      try {
        const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8174'}/api/v1/pricingintelligence/models`, {
          headers: {
            'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
          }
        });
        if (response.ok) {
          const data = await response.json();
          setModels(data);
        } else {
          console.error("Failed to fetch models");
        }
      } catch (err) {
        console.error(err);
      }
    };

    const fetchExperiments = async () => {
        try {
          const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8174'}/api/v1/pricingintelligence/experiments`, {
            headers: {
              'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
            }
          });
          if (response.ok) {
            const data = await response.json();
            setExperiments(data);
          } else {
            console.error("Failed to fetch experiments");
          }
        } catch (err) {
          console.error(err);
        }
    };

    fetchModels();
    fetchExperiments();
  }, []);

  const handleGenerateModel = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8174'}/api/v1/pricingintelligence/models/generate?segment=Enterprise`, {
        method: "POST",
        headers: {
          'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
        }
      });
      if (response.ok) {
        const data = await response.json();
        setModels([...models, data]);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleCreateExperiment = async () => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8174'}/api/v1/pricingintelligence/experiments?name=Test+Exp&segment=Enterprise&controlPrice=100&variantPrice=110`, {
        method: "POST",
        headers: {
          'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
        }
      });
      if (response.ok) {
        const data = await response.json();
        setExperiments([...experiments, data]);
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <main className="min-h-screen p-8 bg-gray-50 text-gray-900 font-sans">
      <header className="mb-8">
        <h1 className="text-3xl font-bold">PricingIntelligence</h1>
        <p className="text-gray-600 mt-2">
          AI pricing optimization platform. Models price elasticity and runs controlled pricing experiments.
        </p>
      </header>

      <div className="mb-8 space-x-4">
        <button onClick={handleGenerateModel} className="px-4 py-2 bg-blue-600 text-white rounded">
          Generate Enterprise Model
        </button>
        <button onClick={handleCreateExperiment} className="px-4 py-2 bg-green-600 text-white rounded">
          Create Test Experiment
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Models Section */}
        <section className="bg-white p-6 rounded-lg shadow border border-gray-200">
          <h2 className="text-xl font-semibold mb-4">Elasticity Models</h2>
          {models.length === 0 ? (
            <p className="text-gray-500 text-sm">No models available.</p>
          ) : (
            <ul className="space-y-4">
              {models.map((m) => (
                <li key={m.id} className="p-4 bg-gray-50 border rounded flex justify-between items-center">
                  <div>
                    <p className="font-medium">{m.segment}</p>
                    <p className="text-sm text-gray-600">Base Price: ${m.basePrice}</p>
                  </div>
                  <div className="text-right text-sm">
                    <p>Elasticity: <span className="font-mono">{m.elasticityCoefficient}</span></p>
                    <p>Confidence: <span className="font-mono">{m.confidenceScore}</span></p>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </section>

        {/* Experiments Section */}
        <section className="bg-white p-6 rounded-lg shadow border border-gray-200">
          <h2 className="text-xl font-semibold mb-4">Pricing Experiments</h2>
          {experiments.length === 0 ? (
            <p className="text-gray-500 text-sm">No experiments running.</p>
          ) : (
            <ul className="space-y-4">
              {experiments.map((ex) => (
                <li key={ex.id} className="p-4 bg-gray-50 border rounded">
                  <div className="flex justify-between mb-2">
                    <p className="font-medium">{ex.name}</p>
                    <span className="text-xs px-2 py-1 bg-green-100 text-green-800 rounded font-bold">
                      {ex.status}
                    </span>
                  </div>
                  <p className="text-sm text-gray-600">Segment: {ex.segment}</p>
                  <p className="text-sm text-gray-600 mt-1">
                    Control: ${ex.controlPrice} vs Variant: ${ex.variantPrice}
                  </p>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </main>
  );
}
