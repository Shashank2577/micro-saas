"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';

export default function Jobs() {
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

  async function addJob() {
    try {
        await api.post('/api/jobs', {
            type: "BATCH"
        });
        const response = await api.get('/api/jobs');
        setJobs(response.data);
    } catch (e) {
        console.error(e);
    }
  }

  async function rollbackJob(id: string) {
    try {
        await api.post(`/api/jobs/${id}/rollback`, {});
        const response = await api.get('/api/jobs');
        setJobs(response.data);
    } catch (e) {
        console.error(e);
    }
  }

  return (
    <main className="p-8">
      <h1 className="text-3xl font-bold mb-6">Sync Jobs</h1>
      <button onClick={addJob} className="bg-blue-500 text-white p-2 mb-4 rounded">Trigger Batch Job</button>
      <ul>
        {jobs.map(j => (
          <li key={j.id} className="border p-2 mb-2">
            Status: {j.status} - Type: {j.type} 
            <button onClick={() => rollbackJob(j.id)} className="bg-red-500 text-white p-1 ml-4 rounded">Rollback</button>
          </li>
        ))}
      </ul>
    </main>
  );
}
