"use client";

import { useState } from "react";
import api from "@/lib/api";
import { useRouter } from "next/navigation";

export default function GeneratePage() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    format: "BLOG_POST",
    tone: "PROFESSIONAL",
    topic: "",
    instructions: ""
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await api.post('/documents/generate', formData);
      router.push(`/documents/${res.data.id}`);
    } catch (err) {
      console.error(err);
      alert('Failed to generate document');
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Generate Content</h1>
      
      <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-sm">
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">Format</label>
          <select 
            className="w-full border border-gray-300 rounded p-2"
            value={formData.format}
            onChange={e => setFormData({...formData, format: e.target.value})}
          >
            <option value="BLOG_POST">Blog Post</option>
            <option value="SOCIAL_MEDIA">Social Media</option>
            <option value="EMAIL">Email</option>
            <option value="MARKETING">Marketing Copy</option>
          </select>
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">Tone</label>
          <select 
            className="w-full border border-gray-300 rounded p-2"
            value={formData.tone}
            onChange={e => setFormData({...formData, tone: e.target.value})}
          >
            <option value="PROFESSIONAL">Professional</option>
            <option value="CASUAL">Casual</option>
            <option value="ENTHUSIASTIC">Enthusiastic</option>
            <option value="PERSUASIVE">Persuasive</option>
          </select>
        </div>

        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-1">Topic</label>
          <input 
            type="text" 
            required
            className="w-full border border-gray-300 rounded p-2"
            placeholder="What should this content be about?"
            value={formData.topic}
            onChange={e => setFormData({...formData, topic: e.target.value})}
          />
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-gray-700 mb-1">Additional Instructions</label>
          <textarea 
            className="w-full border border-gray-300 rounded p-2 h-32"
            placeholder="Any specific keywords or angles?"
            value={formData.instructions}
            onChange={e => setFormData({...formData, instructions: e.target.value})}
          />
        </div>

        <button 
          type="submit" 
          disabled={loading}
          className="w-full bg-indigo-600 text-white font-medium py-2 rounded hover:bg-indigo-700 disabled:opacity-50"
        >
          {loading ? 'Generating...' : 'Generate with AI'}
        </button>
      </form>
    </div>
  );
}
