"use client";

import { useEffect, useState } from "react";
import api from "@/lib/api";

export default function DocumentsPage() {
  const [documents, setDocuments] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/documents')
      .then(res => {
        setDocuments(res.data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  if (loading) return <div>Loading documents...</div>;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">All Documents</h1>
      
      <div className="bg-white rounded shadow-sm overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4">Title</th>
              <th className="p-4">Format</th>
              <th className="p-4">Tone</th>
              <th className="p-4">Status</th>
              <th className="p-4">Date</th>
            </tr>
          </thead>
          <tbody>
            {documents.map(doc => (
              <tr key={doc.id} className="border-b hover:bg-gray-50">
                <td className="p-4"><a href={`/documents/${doc.id}`} className="text-indigo-600 hover:underline">{doc.title}</a></td>
                <td className="p-4">{doc.format}</td>
                <td className="p-4">{doc.tone}</td>
                <td className="p-4">{doc.status}</td>
                <td className="p-4">{new Date(doc.createdAt).toLocaleDateString()}</td>
              </tr>
            ))}
            {documents.length === 0 && (
              <tr>
                <td colSpan={5} className="p-4 text-center text-gray-500">No documents found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
