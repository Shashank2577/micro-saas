"use client";

import { useEffect, useState } from "react";
import api from "@/lib/api";

export default function Home() {
  const [documents, setDocuments] = useState<any[]>([]);

  useEffect(() => {
    api.get('/documents').then(res => setDocuments(res.data.slice(0, 5))).catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Dashboard</h1>
      
      <div className="bg-white p-6 rounded shadow-sm mb-8">
        <h2 className="text-xl font-semibold mb-4">Welcome to GhostWriter</h2>
        <p className="text-gray-600 mb-4">Your AI-powered content writing assistant. Create blog posts, social media updates, and more in seconds.</p>
        <a href="/generate" className="inline-block bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700">Generate Content</a>
      </div>

      <h2 className="text-2xl font-semibold mb-4">Recent Documents</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {documents.map(doc => (
          <a key={doc.id} href={`/documents/${doc.id}`} className="bg-white p-4 rounded shadow-sm hover:shadow-md transition">
            <h3 className="font-semibold text-lg">{doc.title}</h3>
            <p className="text-sm text-gray-500 mb-2">{doc.format} - {doc.tone}</p>
            <span className={`text-xs px-2 py-1 rounded ${doc.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'}`}>
              {doc.status}
            </span>
          </a>
        ))}
      </div>
    </div>
  );
}
