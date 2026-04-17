"use client";

import { useState } from "react";
import { useDropzone } from "react-dropzone";
import { Upload, FileText, Search, MessageSquare } from "lucide-react";

export default function Home() {
  const [documents, setDocuments] = useState<any[]>([]);
  const [uploading, setUploading] = useState(false);
  const [activeTab, setActiveTab] = useState("documents");
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState<any[]>([]);

  const onDrop = async (acceptedFiles: File[]) => {
    setUploading(true);
    for (const file of acceptedFiles) {
      // Mock upload
      const formData = new FormData();
      formData.append("file", file);
      
      try {
        const res = await fetch("http://localhost:8153/api/documents/upload", {
          method: "POST",
          headers: {
            "X-Tenant-ID": "00000000-0000-0000-0000-000000000001",
          },
          body: formData,
        });
        if (res.ok) {
          const doc = await res.json();
          setDocuments((prev) => [...prev, doc]);
        }
      } catch (err) {
        console.error("Upload failed", err);
        // Fallback for UI if backend is not running
        setDocuments((prev) => [...prev, {
          id: Math.random().toString(),
          filename: file.name,
          status: "UPLOADED",
          sizeBytes: file.size,
          uploadedAt: new Date().toISOString()
        }]);
      }
    }
    setUploading(false);
  };

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  const handleSearch = async () => {
    try {
      const res = await fetch("http://localhost:8153/api/documents/search", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Tenant-ID": "00000000-0000-0000-0000-000000000001",
        },
        body: JSON.stringify({ query: searchQuery }),
      });
      if (res.ok) {
        setSearchResults(await res.json());
      }
    } catch (e) {
      console.error(e);
      setSearchResults([{ content: "Mock search result chunk." }]);
    }
  };

  return (
    <main className="flex min-h-screen flex-col bg-gray-50 text-gray-900">
      <header className="bg-white border-b px-8 py-4 flex justify-between items-center shadow-sm">
        <h1 className="text-2xl font-bold flex items-center gap-2">
          <FileText className="text-blue-600" />
          Document Intelligence
        </h1>
        <div className="flex gap-4">
          <button 
            onClick={() => setActiveTab("documents")}
            className={`px-4 py-2 rounded-md font-medium ${activeTab === 'documents' ? 'bg-blue-50 text-blue-700' : 'text-gray-600 hover:bg-gray-100'}`}
          >
            Dashboard
          </button>
          <button 
            onClick={() => setActiveTab("search")}
            className={`px-4 py-2 rounded-md font-medium ${activeTab === 'search' ? 'bg-blue-50 text-blue-700' : 'text-gray-600 hover:bg-gray-100'}`}
          >
            Semantic Search
          </button>
        </div>
      </header>

      <div className="flex-grow p-8 max-w-7xl mx-auto w-full">
        {activeTab === "documents" && (
          <div className="space-y-8">
            <div 
              {...getRootProps()} 
              className={`border-2 border-dashed rounded-xl p-12 text-center cursor-pointer transition-colors
                ${isDragActive ? 'border-blue-500 bg-blue-50' : 'border-gray-300 hover:border-gray-400 bg-white'}`}
            >
              <input {...getInputProps()} />
              <Upload className="mx-auto h-12 w-12 text-gray-400 mb-4" />
              <p className="text-lg font-medium text-gray-700">
                {isDragActive ? "Drop documents here..." : "Drag & drop documents here"}
              </p>
              <p className="text-sm text-gray-500 mt-2">
                Supports PDF, Word, Excel, PNG, JPG (Max 50MB)
              </p>
              {uploading && <p className="text-blue-600 mt-4 font-medium">Uploading...</p>}
            </div>

            <div className="bg-white rounded-xl shadow-sm border overflow-hidden">
              <div className="px-6 py-4 border-b">
                <h2 className="text-lg font-semibold">Recent Documents</h2>
              </div>
              <table className="w-full">
                <thead className="bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  <tr>
                    <th className="px-6 py-3">Filename</th>
                    <th className="px-6 py-3">Status</th>
                    <th className="px-6 py-3">Size</th>
                    <th className="px-6 py-3">Date</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {documents.length === 0 ? (
                    <tr>
                      <td colSpan={4} className="px-6 py-8 text-center text-gray-500">
                        No documents uploaded yet.
                      </td>
                    </tr>
                  ) : (
                    documents.map((doc, idx) => (
                      <tr key={idx} className="hover:bg-gray-50">
                        <td className="px-6 py-4 font-medium text-gray-900 flex items-center gap-2">
                          <FileText size={16} className="text-gray-400" />
                          {doc.filename}
                        </td>
                        <td className="px-6 py-4">
                          <span className="px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-800 font-medium">
                            {doc.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 text-gray-500 text-sm">
                          {(doc.sizeBytes / 1024).toFixed(1)} KB
                        </td>
                        <td className="px-6 py-4 text-gray-500 text-sm">
                          {new Date(doc.uploadedAt).toLocaleDateString()}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {activeTab === "search" && (
          <div className="bg-white rounded-xl shadow-sm border p-6 space-y-6">
            <h2 className="text-xl font-semibold flex items-center gap-2">
              <Search className="text-blue-600" />
              Semantic Search
            </h2>
            <div className="flex gap-4">
              <input 
                type="text" 
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Ask a question or search for concepts across all documents..."
                className="flex-grow border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button 
                onClick={handleSearch}
                className="bg-blue-600 text-white px-6 py-2 rounded-lg font-medium hover:bg-blue-700 transition-colors"
              >
                Search
              </button>
            </div>
            
            <div className="space-y-4 mt-8">
              {searchResults.map((res, i) => (
                <div key={i} className="p-4 border rounded-lg bg-gray-50">
                  <p className="text-gray-800">{res.content}</p>
                </div>
              ))}
              {searchResults.length === 0 && (
                <p className="text-gray-500 text-center py-8">
                  Enter a query to search across your documents.
                </p>
              )}
            </div>
          </div>
        )}
      </div>
    </main>
  );
}
