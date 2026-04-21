"use client";
import { useEffect, useState } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip } from 'recharts';

export default function Growth() {
  const data = [
    { name: 'Day 1', projection: 10000 },
    { name: 'Day 15', projection: 12500 },
    { name: 'Day 30', projection: 15000 },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Growth Projection (30 Days)</h1>
      <div className="bg-white p-6 rounded shadow">
        <AreaChart width={600} height={300} data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Area type="monotone" dataKey="projection" stroke="#8884d8" fill="#8884d8" />
        </AreaChart>
      </div>
    </div>
  );
}
