"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import api from '../../../lib/api';
import SimulatorPanel from '../../../components/SimulatorPanel';
import KnowledgeManager from '../../../components/KnowledgeManager';
import IntentBuilder from '../../../components/IntentBuilder';

export default function AgentBuilderPage() {
  const params = useParams();
  const agentId = params.id as string;
  const [agent, setAgent] = useState<any>(null);
  const [activeTab, setActiveTab] = useState('settings');

  useEffect(() => {
    if (agentId) {
      api.get(`/api/v1/agents/${agentId}`).then(res => setAgent(res.data)).catch(console.error);
    }
  }, [agentId]);

  if (!agent) return <div className="p-8">Loading...</div>;

  return (
    <div className="flex h-screen bg-gray-50">
      <div className="w-64 bg-white border-r p-4">
        <h2 className="text-xl font-bold mb-4">{agent.name}</h2>
        <nav className="space-y-2">
          <button onClick={() => setActiveTab('settings')} className={`w-full text-left p-2 rounded ${activeTab === 'settings' ? 'bg-blue-100 text-blue-700' : 'hover:bg-gray-100'}`}>Settings</button>
          <button onClick={() => setActiveTab('knowledge')} className={`w-full text-left p-2 rounded ${activeTab === 'knowledge' ? 'bg-blue-100 text-blue-700' : 'hover:bg-gray-100'}`}>Knowledge Base</button>
          <button onClick={() => setActiveTab('intents')} className={`w-full text-left p-2 rounded ${activeTab === 'intents' ? 'bg-blue-100 text-blue-700' : 'hover:bg-gray-100'}`}>Intents</button>
          <button onClick={() => setActiveTab('simulator')} className={`w-full text-left p-2 rounded ${activeTab === 'simulator' ? 'bg-blue-100 text-blue-700' : 'hover:bg-gray-100'}`}>Simulator</button>
        </nav>
      </div>

      <div className="flex-1 p-8 overflow-y-auto">
        {activeTab === 'settings' && (
          <div>
            <h3 className="text-2xl font-bold mb-4">Agent Settings</h3>
            <div className="bg-white p-6 rounded shadow space-y-4">
              <div>
                <label className="font-semibold block">System Prompt</label>
                <textarea className="w-full border p-2 rounded mt-1" rows={6} defaultValue={agent.systemPrompt} />
              </div>
              <div>
                <label className="font-semibold block">Voice ID</label>
                <input className="w-full border p-2 rounded mt-1" defaultValue={agent.voiceId} />
              </div>
              <button className="bg-blue-600 text-white px-4 py-2 rounded">Save Changes</button>
            </div>
          </div>
        )}
        {activeTab === 'knowledge' && <KnowledgeManager agentId={agentId} />}
        {activeTab === 'intents' && <IntentBuilder agentId={agentId} />}
        {activeTab === 'simulator' && <SimulatorPanel agentId={agentId} />}
      </div>
    </div>
  );
}
