"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import api from '../../../lib/api';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

export default function SkuDetails() {
  const params = useParams();
  const [sku, setSku] = useState<any>(null);
  const [forecasts, setForecasts] = useState<any[]>([]);

  useEffect(() => {
    async function load() {
      try {
        const [skuRes, forecastRes] = await Promise.all([
          api.get(`/api/skus/${params.id}`),
          api.get(`/api/forecasts?skuId=${params.id}`)
        ]);
        setSku(skuRes.data);
        
        // Sort forecasts by date
        const sorted = forecastRes.data.sort((a: any, b: any) => new Date(a.forecastDate).getTime() - new Date(b.forecastDate).getTime());
        setForecasts(sorted);
      } catch (err) {
        console.error("Failed to load details", err);
      }
    }
    load();
  }, [params.id]);

  const generateForecast = async () => {
    try {
      await api.post(`/api/forecasts/generate?skuId=${params.id}`);
      const res = await api.get(`/api/forecasts?skuId=${params.id}`);
      const sorted = res.data.sort((a: any, b: any) => new Date(a.forecastDate).getTime() - new Date(b.forecastDate).getTime());
      setForecasts(sorted);
    } catch(err) {
      console.error(err);
    }
  };

  const chartData = {
    labels: forecasts.map(f => f.forecastDate),
    datasets: [
      {
        label: 'Predicted Demand',
        data: forecasts.map(f => f.predictedDemand),
        borderColor: 'rgb(53, 162, 235)',
        backgroundColor: 'rgba(53, 162, 235, 0.5)',
      }
    ]
  };

  if (!sku) return <div>Loading...</div>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">{sku.name} ({sku.skuCode}) Details</h1>
      
      <div className="grid grid-cols-2 gap-4">
        <div>
          <p><strong>Category:</strong> {sku.category}</p>
          <p><strong>Stock:</strong> {sku.stockQuantity}</p>
        </div>
        <div>
          <p><strong>Cost Price:</strong> ${sku.costPrice}</p>
          <p><strong>Current Price:</strong> ${sku.currentPrice}</p>
        </div>
      </div>

      <div className="border p-4 rounded">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Demand Forecast</h2>
          <button onClick={generateForecast} className="px-4 py-2 bg-blue-600 text-white rounded">
            Generate Forecast (AI)
          </button>
        </div>
        
        {forecasts.length > 0 ? (
          <Line options={{ responsive: true }} data={chartData} />
        ) : (
          <p>No forecasts generated yet.</p>
        )}
      </div>
    </div>
  );
}
