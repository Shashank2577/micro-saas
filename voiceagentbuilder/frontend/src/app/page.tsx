"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import api from '../lib/api';

export default function DashboardPage() {
  const [agents, setAgents] = useState([]);
  const [callLogs, setCallLogs] = useState([]);

  useEffect(() => {
    api.get('/api/v1/agents').then(res => setAgents(res.data)).catch(console.error);
    api.get('/api/v1/calls').then(res => setCallLogs(res.data)).catch(console.error);
  }, []);

  const totalCalls = callLogs.length;
  const totalDuration = callLogs.reduce((acc, call: any) => acc + (call.durationSeconds || 0), 0);
  const totalCost = callLogs.reduce((acc, call: any) => acc + (call.cost || 0), 0);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">VoiceAgentBuilder Dashboard</h1>
      
      <div className="grid grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-2">Total Agents</h2>
          <p className="text-4xl">{agents.length}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-2">Total Calls</h2>
          <p className="text-4xl">{totalCalls}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-2">Total Duration (min)</h2>
          <p className="text-4xl">{Math.round(totalDuration / 60)}</p>
        </div>
      </div>

      <div className="mb-6 flex justify-between items-center">
        <h2 className="text-2xl font-bold">Your Agents</h2>
        <Link href="/agents/new" className="bg-blue-600 text-white px-4 py-2 rounded">
          Create New Agent
        </Link>
      </div>

      <div className="grid grid-cols-2 gap-4">
        {agents.map((agent: any) => (
          <Link href={`/agents/${agent.id}`} key={agent.id} className="block">
            <div className="bg-white p-4 rounded-lg shadow hover:shadow-md transition">
              <h3 className="text-xl font-semibold">{agent.name}</h3>
              <p className="text-gray-600">{agent.description}</p>
              <div className="mt-2 text-sm text-gray-500">
                Status: {agent.status} | Voice: {agent.voiceId || 'Default'}
              </div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}
