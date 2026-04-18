"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';

export default function LearnerProfilePage() {
  const [backgroundInfo, setBackgroundInfo] = useState('');
  const [learningStyle, setLearningStyle] = useState('VISUAL');
  const [saving, setSaving] = useState(false);

  // In a real app, this would come from auth context
  const mockUserId = '11111111-1111-1111-1111-111111111111';

  useEffect(() => {
    async function loadProfile() {
      try {
        const res = await api.get(`/api/learners/${mockUserId}`);
        if (res.data) {
          setBackgroundInfo(res.data.backgroundInfo || '');
          setLearningStyle(res.data.learningStyle || 'VISUAL');
        }
      } catch (error) {
        console.log('No existing profile or error loading profile', error);
      }
    }
    loadProfile();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    try {
      await api.post('/api/learners', {
        userId: mockUserId,
        backgroundInfo,
        learningStyle
      });
      alert('Profile saved successfully!');
    } catch (error) {
      console.error('Error saving profile', error);
      alert('Failed to save profile');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6 text-gray-900">Learner Profile</h1>
      <form onSubmit={handleSubmit} className="bg-white p-6 shadow rounded-lg border border-gray-200">
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">Background Information</label>
          <textarea 
            className="w-full border border-gray-300 rounded px-3 py-2 text-gray-800"
            rows={4}
            value={backgroundInfo}
            onChange={(e) => setBackgroundInfo(e.target.value)}
            placeholder="Tell us about your educational background and goals..."
          />
        </div>
        <div className="mb-6">
          <label className="block text-gray-700 font-medium mb-2">Preferred Learning Style</label>
          <select 
            className="w-full border border-gray-300 rounded px-3 py-2 text-gray-800"
            value={learningStyle}
            onChange={(e) => setLearningStyle(e.target.value)}
          >
            <option value="VISUAL">Visual (Images, Diagrams, Videos)</option>
            <option value="AUDITORY">Auditory (Listening, Discussing)</option>
            <option value="READING_WRITING">Reading & Writing</option>
            <option value="KINESTHETIC">Kinesthetic (Doing, Practice)</option>
          </select>
        </div>
        <button 
          type="submit" 
          disabled={saving}
          className="w-full bg-indigo-600 text-white font-medium py-2 px-4 rounded hover:bg-indigo-700 transition disabled:bg-indigo-400"
        >
          {saving ? 'Saving...' : 'Save Profile'}
        </button>
      </form>
    </div>
  );
}
