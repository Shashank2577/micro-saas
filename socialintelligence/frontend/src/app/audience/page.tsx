"use client";
import { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

export default function Audience() {
  const data = [
    { name: '18-24', percentage: 40 },
    { name: '25-34', percentage: 35 },
    { name: '35-44', percentage: 15 },
    { name: '45+', percentage: 10 },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Audience Demographics</h1>
      <div className="bg-white p-6 rounded shadow">
        <h3 className="text-lg font-bold mb-4">Age Distribution</h3>
        <BarChart width={600} height={300} data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="percentage" fill="#8884d8" />
        </BarChart>
      </div>
    </div>
  );
}
