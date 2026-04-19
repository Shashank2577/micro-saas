'use client'

import React, { useState, useEffect } from 'react';

export default function OptionPoolPlansPage() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch('/api/v1/equity/option-pool-plans')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch');
        return res.json();
      })
      .then(data => {
        setData(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">OptionPoolPlans</h1>
      <table className="w-full border-collapse">
        <thead>
          <tr className="border-b text-left">
            <th className="py-2">Name</th>
            <th className="py-2">Status</th>
            <th className="py-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={3} className="py-4 text-center text-gray-500">No data found</td>
            </tr>
          ) : (
            data.map((item: any) => (
              <tr key={item.id} className="border-b">
                <td className="py-2">{item.name}</td>
                <td className="py-2">{item.status}</td>
                <td className="py-2">
                  <a href={`/dashboard/option-pool-plans/${item.id}`} className="text-blue-500 hover:underline">View</a>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
