"use client";

import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';

export default function ExperimentsPage() {
  const [experiments, setExperiments] = useState<any[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    startDate: '',
    endDate: '',
  });

  useEffect(() => {
    fetch('/api/pricing/experiments')
      .then(res => res.json())
      .then(data => setExperiments(data))
      .catch(console.error);
  }, []);

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const payload = {
        name: formData.name,
        startDate: formData.startDate,
        endDate: formData.endDate,
        status: 'PENDING',
        variants: ['Control ($50)', 'Variant A ($55)']
      };
      const res = await fetch('/api/pricing/experiments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      setExperiments(prev => [...prev, data]);
      setShowForm(false);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Pricing Experiments (A/B Tests)</h1>
        <button
          onClick={() => setShowForm(!showForm)}
          className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          {showForm ? 'Cancel' : 'Create New Experiment'}
        </button>
      </div>

      {showForm && (
        <Card className="mb-8 border-blue-200">
          <CardHeader>
            <CardTitle>New Experiment Setup</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleCreate} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1">Experiment Name</label>
                <input
                  type="text"
                  className="border rounded p-2 w-full"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({...formData, name: e.target.value})}
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Start Date</label>
                  <input
                    type="date"
                    className="border rounded p-2 w-full"
                    required
                    value={formData.startDate}
                    onChange={(e) => setFormData({...formData, startDate: e.target.value})}
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">End Date</label>
                  <input
                    type="date"
                    className="border rounded p-2 w-full"
                    required
                    value={formData.endDate}
                    onChange={(e) => setFormData({...formData, endDate: e.target.value})}
                  />
                </div>
              </div>
              <button type="submit" className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                Save & Deploy
              </button>
            </form>
          </CardContent>
        </Card>
      )}

      <div className="space-y-4">
        {experiments.length === 0 ? (
          <p className="text-gray-500">No active experiments found. Start a new one to test price points.</p>
        ) : (
          experiments.map((exp: any) => (
            <Card key={exp.id}>
              <CardHeader>
                <CardTitle className="flex justify-between">
                  <span>{exp.name}</span>
                  <span className={`text-sm px-2 py-1 rounded ${exp.status === 'RUNNING' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                    {exp.status}
                  </span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-gray-500 mb-4">Dates: {exp.startDate} to {exp.endDate}</p>
                <div className="grid grid-cols-2 gap-4">
                  <div className="border p-4 rounded">
                    <h3 className="font-semibold mb-2">Control</h3>
                    <p>Price: $50.00</p>
                    <p>Conversions: 150</p>
                  </div>
                  <div className="border p-4 rounded bg-blue-50">
                    <h3 className="font-semibold mb-2">Variant A (+10%)</h3>
                    <p>Price: $55.00</p>
                    <p>Conversions: 142</p>
                    <p className="text-green-600 font-semibold mt-2">Revenue Lift: +4.8%</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>
    </div>
  );
}
