import React from 'react';

export default function Home() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">NotificationHub Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-2">Delivery Stats</h2>
          <p className="text-gray-600">Total sent today: 1,200</p>
          <p className="text-gray-600">Failed: 12</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-2">Open Rates</h2>
          <p className="text-gray-600">Average: 45%</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-2">Click Rates</h2>
          <p className="text-gray-600">Average: 12%</p>
        </div>
      </div>
    </div>
  );
}
