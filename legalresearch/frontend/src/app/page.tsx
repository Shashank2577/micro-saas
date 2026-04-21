"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../lib/api';
import ResearchQueryForm from '../components/ResearchQueryForm';

export default function DashboardPage() {
  const [queries, setQueries] = useState<any[]>([]);
  const [memos, setMemos] = useState<any[]>([]);
  const [workflows, setWorkflows] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [queriesData, memosData, workflowsData] = await Promise.all([
          api.getQueries().catch(() => []),
          api.getMemos().catch(() => []),
          api.getWorkflows().catch(() => [])
        ]);
        setQueries(queriesData.slice(0, 5) || []);
        setMemos(memosData.slice(0, 5) || []);
        setWorkflows(workflowsData.slice(0, 5) || []);
      } catch (error) {
        console.error("Error fetching dashboard data", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleQuerySubmit = async (data: any) => {
    try {
      await api.createQuery(data);
      // Refresh queries
      const queriesData = await api.getQueries();
      setQueries(queriesData.slice(0, 5) || []);
    } catch (error) {
      console.error("Error creating query", error);
    }
  };

  if (loading) return <div>Loading dashboard...</div>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <h2 className="text-xl font-semibold mb-4">New Research Query</h2>
          <ResearchQueryForm onSubmit={handleQuerySubmit} />
        </div>

        <div className="space-y-6">
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Recent Memos</h2>
            {memos.length === 0 ? (
              <p className="text-gray-500">No memos found.</p>
            ) : (
              <ul className="space-y-2">
                {memos.map(memo => (
                  <li key={memo.id} className="border-b pb-2">
                    <a href={`/memos/${memo.id}`} className="text-indigo-600 hover:text-indigo-800">
                      {memo.title || 'Untitled Memo'}
                    </a>
                    <span className="ml-2 text-sm text-gray-500">({memo.status})</span>
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Active Workflows</h2>
            {workflows.length === 0 ? (
              <p className="text-gray-500">No active workflows.</p>
            ) : (
              <ul className="space-y-2">
                {workflows.map(workflow => (
                  <li key={workflow.id} className="border-b pb-2">
                    <span className="font-medium">{workflow.name}</span>
                    <span className="ml-2 text-sm text-gray-500">Status: {workflow.status}</span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
