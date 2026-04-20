'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import api from '@/lib/api';

export default function NewProjectPage() {
  const router = useRouter();
  const [file, setFile] = useState<File | null>(null);
  const [title, setTitle] = useState('');
  const [uploading, setUploading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!file || !title) return;

    setUploading(true);
    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', title);

    try {
      const res = await api.post('/projects', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      router.push(`/projects/${res.data.id}`);
    } catch (err) {
      console.error('Failed to upload project', err);
      setUploading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto bg-white p-6 rounded-lg shadow">
      <h2 className="text-2xl font-bold mb-4">Create New Project</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Project Title</label>
          <input
            type="text"
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            placeholder="My Awesome Video"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700">Video File</label>
          <input
            type="file"
            accept="video/mp4,video/webm,video/quicktime"
            className="mt-1 block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
            onChange={(e) => setFile(e.target.files?.[0] || null)}
            required
          />
        </div>

        <button
          type="submit"
          disabled={uploading || !file || !title}
          className="w-full bg-blue-600 text-white py-2 px-4 rounded disabled:bg-gray-400 hover:bg-blue-700 transition"
        >
          {uploading ? 'Uploading...' : 'Create Project'}
        </button>
      </form>
    </div>
  );
}
