'use client';
import React, { useState } from 'react';

interface SubtitleListProps {
  subtitles: any[];
  currentTime: number;
  onUpdateSubtitle: (id: string, data: any) => Promise<void>;
}

export default function SubtitleList({ subtitles, currentTime, onUpdateSubtitle }: SubtitleListProps) {
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editContent, setEditContent] = useState('');

  const startEdit = (sub: any) => {
    setEditingId(sub.id);
    setEditContent(sub.content);
  };

  const saveEdit = async (sub: any) => {
    await onUpdateSubtitle(sub.id, {
      startTimeMs: sub.startTimeMs,
      endTimeMs: sub.endTimeMs,
      content: editContent
    });
    setEditingId(null);
  };

  return (
    <div className="space-y-2">
      {subtitles.map(sub => {
        const isActive = currentTime >= sub.startTimeMs && currentTime <= sub.endTimeMs;

        return (
          <div key={sub.id} className={`p-2 border rounded text-sm ${isActive ? 'bg-yellow-50 border-yellow-300' : 'bg-gray-50'}`}>
            <div className="flex justify-between text-xs text-gray-500 mb-1">
              <span>{(sub.startTimeMs/1000).toFixed(1)}s - {(sub.endTimeMs/1000).toFixed(1)}s</span>
            </div>
            {editingId === sub.id ? (
              <div>
                <textarea
                  value={editContent}
                  onChange={e => setEditContent(e.target.value)}
                  className="w-full border p-1 rounded text-sm"
                  rows={2}
                />
                <div className="flex gap-2 mt-1">
                  <button onClick={() => saveEdit(sub)} className="bg-blue-600 text-white px-2 py-1 rounded text-xs">Save</button>
                  <button onClick={() => setEditingId(null)} className="bg-gray-300 px-2 py-1 rounded text-xs">Cancel</button>
                </div>
              </div>
            ) : (
              <div
                className="cursor-pointer hover:bg-gray-100 p-1 rounded"
                onClick={() => startEdit(sub)}
              >
                {sub.content}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
}
