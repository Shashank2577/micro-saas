"use client";

import { useEffect, useState } from 'react';
import api from '../lib/api';

export default function KnowledgeManager({ agentId }: { agentId: string }) {
  const [docs, setDocs] = useState([]);
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const loadDocs = async () => {
    try {
      const res = await api.get(`/api/v1/agents/${agentId}/documents`);
      setDocs(res.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => { loadDocs(); }, [agentId]);

  const handleAdd = async () => {
    if (!title || !content) return;
    try {
      await api.post(`/api/v1/agents/${agentId}/documents`, { title, content });
      setTitle('');
      setContent('');
      loadDocs();
    } catch (e) {
      console.error(e);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await api.delete(`/api/v1/agents/${agentId}/documents/${id}`);
      loadDocs();
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div>
      <h3 className="text-2xl font-bold mb-4">Knowledge Base</h3>
      <div className="bg-white p-6 rounded shadow mb-6 space-y-4">
        <h4 className="font-semibold text-lg">Add Document</h4>
        <input className="w-full border p-2 rounded" placeholder="Title" value={title} onChange={e => setTitle(e.target.value)} />
        <textarea className="w-full border p-2 rounded" rows={4} placeholder="Content" value={content} onChange={e => setContent(e.target.value)} />
        <button onClick={handleAdd} className="bg-green-600 text-white px-4 py-2 rounded">Add to Knowledge</button>
      </div>

      <div className="space-y-4">
        {docs.map((doc: any) => (
          <div key={doc.id} className="bg-white p-4 rounded shadow flex justify-between items-start">
            <div>
              <h5 className="font-bold">{doc.title}</h5>
              <p className="text-gray-600 text-sm mt-1 line-clamp-2">{doc.content}</p>
            </div>
            <button onClick={() => handleDelete(doc.id)} className="text-red-600 hover:underline">Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
}
