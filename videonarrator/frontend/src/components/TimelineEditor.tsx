'use client';
import React from 'react';

interface TimelineEditorProps {
  subtitles: any[];
  currentTime: number;
}

export default function TimelineEditor({ subtitles, currentTime }: TimelineEditorProps) {
  // A simplified timeline mock
  const DURATION = 60000; // Assume 60s for mock timeline scale

  return (
    <div>
      <h3 className="font-semibold mb-2">Timeline</h3>
      <div className="relative h-20 bg-gray-100 border rounded">
        {/* Playhead */}
        <div
          className="absolute top-0 bottom-0 w-0.5 bg-red-500 z-10"
          style={{ left: `${(currentTime / DURATION) * 100}%` }}
        />

        {/* Subtitle blocks */}
        {subtitles.map(sub => {
          const left = (sub.startTimeMs / DURATION) * 100;
          const width = ((sub.endTimeMs - sub.startTimeMs) / DURATION) * 100;
          return (
            <div
              key={sub.id}
              className="absolute h-8 bg-blue-300 border border-blue-400 rounded top-6 flex items-center overflow-hidden px-1 text-xs whitespace-nowrap"
              style={{ left: `${left}%`, width: `${width}%` }}
              title={sub.content}
            >
              {sub.content}
            </div>
          );
        })}
      </div>
    </div>
  );
}
