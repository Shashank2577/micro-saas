"use client";

import { useEffect, useState } from "react";
import api from "@/lib/api";
import { useRouter } from "next/navigation";

export default function DocumentEditorPage({ params }: { params: { id: string } }) {
  const router = useRouter();
  const [doc, setDoc] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  
  // local edits
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  useEffect(() => {
    api.get(`/documents/${params.id}`)
      .then(res => {
        setDoc(res.data);
        setTitle(res.data.title);
        setContent(res.data.content || "");
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, [params.id]);

  const handleSave = async () => {
    setSaving(true);
    try {
      await api.put(`/documents/${params.id}`, { title, content });
      alert("Saved successfully!");
    } catch (err) {
      console.error(err);
      alert("Failed to save");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm("Are you sure you want to delete this document?")) return;
    try {
      await api.delete(`/documents/${params.id}`);
      router.push("/documents");
    } catch (err) {
      console.error(err);
      alert("Failed to delete");
    }
  };

  if (loading) return <div>Loading document...</div>;
  if (!doc) return <div>Document not found.</div>;

  return (
    <div className="flex flex-col h-full max-w-5xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <input 
          className="text-3xl font-bold bg-transparent border-none focus:ring-0 w-2/3"
          value={title}
          onChange={e => setTitle(e.target.value)}
        />
        <div className="space-x-2">
          <button 
            onClick={handleDelete}
            className="px-4 py-2 text-red-600 hover:bg-red-50 rounded"
          >
            Delete
          </button>
          <button 
            onClick={handleSave}
            disabled={saving}
            className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 disabled:opacity-50"
          >
            {saving ? 'Saving...' : 'Save Changes'}
          </button>
        </div>
      </div>

      <div className="flex gap-4 text-sm text-gray-500 mb-6">
        <span>Format: {doc.format}</span>
        <span>Tone: {doc.tone}</span>
        <span>Status: {doc.status}</span>
      </div>

      <div className="flex-1 bg-white rounded shadow-sm flex flex-col p-4">
        <textarea 
          className="w-full flex-1 resize-none border-0 focus:ring-0 p-4"
          value={content}
          onChange={e => setContent(e.target.value)}
          placeholder="Start writing..."
        />
      </div>
    </div>
  );
}
