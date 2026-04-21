"use client";

import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

export default function DashboardPage() {
  const [segments, setSegments] = useState([]);

  useEffect(() => {
    fetch('/api/pricing/segments')
      .then(res => res.json())
      .then(data => setSegments(data))
      .catch(console.error);
  }, []);

  const dummyData = [
    { name: 'Jan', lift: 4000, churn: 2400 },
    { name: 'Feb', lift: 3000, churn: 1398 },
    { name: 'Mar', lift: 2000, churn: 9800 },
    { name: 'Apr', lift: 2780, churn: 3908 },
    { name: 'May', lift: 1890, churn: 4800 },
    { name: 'Jun', lift: 2390, churn: 3800 },
    { name: 'Jul', lift: 3490, churn: 4300 },
  ];

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">Pricing Intelligence Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <Card>
          <CardHeader>
            <CardTitle>Active Segments</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold">{segments.length || 3}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Total Revenue Lift</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold text-green-600">+15.2%</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Active Experiments</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold text-blue-600">4</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Churn Impact</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-2xl font-bold text-red-600">-2.1%</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Revenue Lift vs Churn Over Time</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="h-96">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={dummyData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis yAxisId="left" />
                <YAxis yAxisId="right" orientation="right" />
                <Tooltip />
                <Legend />
                <Line yAxisId="left" type="monotone" dataKey="lift" stroke="#8884d8" name="Revenue Lift ($)" />
                <Line yAxisId="right" type="monotone" dataKey="churn" stroke="#82ca9d" name="Churn Risk" />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
