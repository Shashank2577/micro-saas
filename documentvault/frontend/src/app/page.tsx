"use client";

import { useEffect, useState } from "react";
import { Folder, File, Upload, Share2, Search, Trash2, Eye } from "lucide-react";
import api from "../lib/api";

interface Document {
  id: string;
  title: string;
  description: string;
  sizeBytes: number;
  status: string;
  createdAt: string;
}

export default function DocumentVaultDashboard() {
  const [documents, setDocuments] = useState<Document[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  const fetchDocuments = async () => {
    try {
      setLoading(true);
      const res = await api.get('/api/documents', { params: { search } });
      if (res.data && res.data.content) {
        setDocuments(res.data.content);
      } else {
        setDocuments([]);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDocuments();
  }, [search]);

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("title", file.name);

    try {
      await api.post("/api/documents/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });
      fetchDocuments();
    } catch (err) {
      console.error("Upload failed", err);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await api.delete(`/api/documents/${id}`);
      fetchDocuments();
    } catch (err) {
      console.error("Delete failed", err);
    }
  };

  return (
    <div className="container mx-auto p-6 max-w-6xl">
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-gray-900">Document Vault</h1>
          <p className="text-gray-500 mt-2">Securely store, manage, and share your documents.</p>
        </div>
        <div className="flex gap-4">
          <div className="relative">
            <input
              type="text"
              placeholder="Search documents..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="pl-10 pr-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
          </div>
          <label className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 cursor-pointer">
            <Upload className="h-5 w-5" />
            Upload File
            <input data-testid="upload-input" type="file" className="hidden" onChange={handleUpload} />
          </label>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow border border-gray-200">
        <div className="p-4 border-b border-gray-200 font-medium grid grid-cols-12 gap-4 text-gray-700 bg-gray-50">
          <div className="col-span-5">Name</div>
          <div className="col-span-2">Size</div>
          <div className="col-span-3">Uploaded</div>
          <div className="col-span-2 text-right">Actions</div>
        </div>

        {loading ? (
          <div className="p-8 text-center text-gray-500">Loading documents...</div>
        ) : documents.length === 0 ? (
          <div className="p-12 text-center text-gray-500 flex flex-col items-center">
            <Folder className="h-12 w-12 text-gray-300 mb-4" />
            <p>No documents found. Upload a file to get started.</p>
          </div>
        ) : (
          <ul className="divide-y divide-gray-200">
            {documents.map((doc) => (
              <li key={doc.id} className="p-4 grid grid-cols-12 gap-4 items-center hover:bg-gray-50 transition-colors">
                <div className="col-span-5 flex items-center gap-3">
                  <File className="h-6 w-6 text-blue-500" />
                  <span className="font-medium text-gray-900 truncate">{doc.title}</span>
                </div>
                <div className="col-span-2 text-gray-500 text-sm">
                  {(doc.sizeBytes / 1024).toFixed(1)} KB
                </div>
                <div className="col-span-3 text-gray-500 text-sm">
                  {new Date(doc.createdAt).toLocaleDateString()}
                </div>
                <div className="col-span-2 flex items-center justify-end gap-3 text-gray-400">
                  <button className="hover:text-blue-600" title="Preview">
                    <Eye className="h-5 w-5" />
                  </button>
                  <button className="hover:text-green-600" title="Share">
                    <Share2 className="h-5 w-5" />
                  </button>
                  <button className="hover:text-red-600" title="Delete" onClick={() => handleDelete(doc.id)}>
                    <Trash2 className="h-5 w-5" />
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
