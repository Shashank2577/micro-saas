"use client";

import { useState } from 'react';
import api from '../lib/api';

export default function SimulatorPanel({ agentId }: { agentId: string }) {
  const [messages, setMessages] = useState<{role: string, content: string}[]>([]);
  const [input, setInput] = useState('');

  const handleSend = async () => {
    if (!input.trim()) return;
    
    const newMessages = [...messages, { role: 'user', content: input }];
    setMessages(newMessages);
    setInput('');

    try {
      const res = await api.post(`/api/v1/agents/${agentId}/simulate`, { message: input });
      setMessages([...newMessages, { role: 'agent', content: res.data.reply }]);
    } catch (error) {
      console.error(error);
      setMessages([...newMessages, { role: 'system', content: 'Error simulating response.' }]);
    }
  };

  return (
    <div className="flex flex-col h-full bg-white rounded shadow">
      <div className="p-4 border-b">
        <h3 className="text-xl font-bold">Simulator</h3>
      </div>
      <div className="flex-1 p-4 overflow-y-auto space-y-4">
        {messages.map((m, i) => (
          <div key={i} className={`p-3 rounded-lg ${m.role === 'user' ? 'bg-blue-100 ml-auto max-w-[80%]' : 'bg-gray-100 mr-auto max-w-[80%]'}`}>
            <span className="font-semibold text-xs text-gray-500 uppercase">{m.role}</span>
            <p>{m.content}</p>
          </div>
        ))}
      </div>
      <div className="p-4 border-t flex gap-2">
        <input 
          className="flex-1 border p-2 rounded" 
          value={input} 
          onChange={e => setInput(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && handleSend()}
          placeholder="Type a message to simulate..."
        />
        <button onClick={handleSend} className="bg-blue-600 text-white px-4 py-2 rounded">Send</button>
      </div>
    </div>
  );
}
