"use client";

import Link from 'next/link';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

export default function Home() {
  const chartData = {
    labels: ['Ready', 'Pending Validation', 'Failed Checks'],
    datasets: [
      {
        data: [85, 10, 5],
        backgroundColor: [
          'rgba(34, 197, 94, 0.8)', // Green
          'rgba(234, 179, 8, 0.8)', // Yellow
          'rgba(239, 68, 68, 0.8)', // Red
        ],
        borderColor: [
          'rgba(34, 197, 94, 1)',
          'rgba(234, 179, 8, 1)',
          'rgba(239, 68, 68, 1)',
        ],
        borderWidth: 1,
      },
    ],
  };

  const chartOptions = {
    cutout: '70%',
    plugins: {
        legend: {
            position: 'bottom' as const
        }
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6 text-gray-800">Data Lineage Tracker</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Link href="/assets" className="block p-6 bg-white rounded-lg border shadow hover:bg-gray-50 transition">
          <h2 className="text-xl font-bold mb-2">Data Assets</h2>
          <p className="text-gray-600">Browse and manage data assets, schemas, and classifications.</p>
        </Link>
        <Link href="/lineage" className="block p-6 bg-white rounded-lg border shadow hover:bg-gray-50 transition">
          <h2 className="text-xl font-bold mb-2">Lineage Graph</h2>
          <p className="text-gray-600">Visualize upstream and downstream data dependencies.</p>
        </Link>
        <Link href="/governance" className="block p-6 bg-white rounded-lg border shadow hover:bg-gray-50 transition">
          <h2 className="text-xl font-bold mb-2">Governance</h2>
          <p className="text-gray-600">Define and manage compliance and access policies.</p>
        </Link>
        <Link href="/audit" className="block p-6 bg-white rounded-lg border shadow hover:bg-gray-50 transition">
          <h2 className="text-xl font-bold mb-2">Audit Trails</h2>
          <p className="text-gray-600">Review access logs and incident reports.</p>
        </Link>
      </div>
      
      <div className="mt-12 grid grid-cols-1 md:grid-cols-2 gap-8">
        <div>
            <h2 className="text-2xl font-bold mb-4">Compliance Readiness</h2>
            <div className="bg-white p-6 rounded-lg border shadow flex flex-col space-y-6">
               <div className="flex justify-between items-center border-b pb-4">
                 <div className="text-gray-500">GDPR Readiness</div>
                 <div className="text-2xl font-bold text-green-600">85%</div>
               </div>
               <div className="flex justify-between items-center border-b pb-4">
                 <div className="text-gray-500">HIPAA Readiness</div>
                 <div className="text-2xl font-bold text-yellow-600">60%</div>
               </div>
               <div className="flex justify-between items-center">
                 <div className="text-gray-500">CCPA Readiness</div>
                 <div className="text-2xl font-bold text-green-600">90%</div>
               </div>
            </div>
        </div>
        
        <div>
            <h2 className="text-2xl font-bold mb-4">Overall Data Governance Health</h2>
            <div className="bg-white p-6 rounded-lg border shadow flex justify-center h-64">
                <Doughnut data={chartData} options={chartOptions} />
            </div>
        </div>
      </div>
    </div>
  );
}
