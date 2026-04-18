"use client";

import { useEffect, useState } from 'react';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';
import { Bar } from 'react-chartjs-2';
import api from '../lib/api';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

export default function Home() {
  const [jobs, setJobs] = useState<any[]>([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await api.get('/api/jobs');
        setJobs(response.data);
      } catch (e) {
        console.error(e);
      }
    }
    fetchData();
  }, []);

  const data = {
    labels: jobs.map(j => j.id.substring(0, 8)),
    datasets: [
      {
        label: 'Records Processed',
        data: jobs.map(j => j.recordsProcessed),
        backgroundColor: 'rgba(53, 162, 235, 0.5)',
      },
    ],
  };

  return (
    <main className="p-8">
      <h1 className="text-3xl font-bold mb-6">DataUnification Dashboard</h1>
      <div className="mb-8 w-1/2">
        <h2 className="text-xl mb-4">Sync Performance</h2>
        {jobs.length > 0 ? <Bar data={data} /> : <p>No job data.</p>}
      </div>
      <div>
        <h2 className="text-xl mb-4">Recent Sync Jobs</h2>
        <ul>
          {jobs.map(job => (
            <li key={job.id} className="border p-2 mb-2">
              Status: {job.status} | Processed: {job.recordsProcessed} | Type: {job.type}
            </li>
          ))}
        </ul>
      </div>
    </main>
  );
}
