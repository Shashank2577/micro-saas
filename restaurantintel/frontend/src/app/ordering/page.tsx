"use client";

import { useState, useEffect } from 'react';
import api from '../../lib/api';

interface Ingredient {
  id: string;
  name: string;
  unit: string;
  currentStock: number;
  parLevel: number;
}

interface PredictiveOrder {
  id: string;
  ingredient: Ingredient;
  predictedDemand: number;
  recommendedOrderAmount: number;
  status: string;
  aiRationale: string;
}

export default function PredictiveOrdering() {
  const [orders, setOrders] = useState<PredictiveOrder[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const res = await api.get('/api/v1/orders/predictive');
      setOrders(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const generateOrders = async () => {
    setLoading(true);
    try {
      await api.post('/api/v1/orders/predictive/generate');
      await fetchOrders();
    } catch (err) {
      console.error(err);
      setLoading(false);
    }
  };

  const updateStatus = async (id: string, status: string) => {
    try {
      await api.put(`/api/v1/orders/predictive/${id}/status`, { status });
      fetchOrders();
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <div className="p-8">Loading...</div>;

  return (
    <div className="p-8 max-w-5xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Predictive Ordering</h1>
        <button onClick={generateOrders} className="bg-green-600 text-white px-4 py-2 rounded">
          Generate Predictions
        </button>
      </div>

      <div className="space-y-6">
        {orders.length === 0 ? (
          <p className="text-gray-500">No pending orders. Generate predictions to get AI recommendations.</p>
        ) : (
          orders.map(order => (
            <div key={order.id} className="bg-white p-6 rounded-lg shadow border border-gray-200">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h3 className="text-xl font-semibold">{order.ingredient?.name || 'Unknown Ingredient'}</h3>
                  <p className="text-sm text-gray-500">Current Stock: {order.ingredient?.currentStock} {order.ingredient?.unit} | Par: {order.ingredient?.parLevel}</p>
                </div>
                <span className={`px-3 py-1 rounded text-sm font-medium ${
                  order.status === 'APPROVED' ? 'bg-green-100 text-green-800' :
                  order.status === 'REJECTED' ? 'bg-red-100 text-red-800' :
                  'bg-yellow-100 text-yellow-800'
                }`}>
                  {order.status}
                </span>
              </div>
              
              <div className="bg-gray-50 p-4 rounded mb-4">
                <p className="font-medium">Recommended Order: {order.recommendedOrderAmount} {order.ingredient?.unit}</p>
                <p className="text-sm text-gray-600 mt-1"><span className="font-semibold text-gray-800">AI Rationale:</span> {order.aiRationale}</p>
              </div>

              {order.status === 'PENDING' && (
                <div className="flex space-x-3">
                  <button onClick={() => updateStatus(order.id, 'APPROVED')} className="bg-blue-600 text-white px-4 py-2 rounded text-sm">Approve</button>
                  <button onClick={() => updateStatus(order.id, 'REJECTED')} className="bg-gray-200 text-gray-800 px-4 py-2 rounded text-sm">Reject</button>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
