import React from 'react';

export default function MilestoneTrackDetail({ track }: { track: any }) {
  if (!track) return <div>No track selected</div>;
  return (
    <div className="p-4 border rounded">
      <h2 className="text-xl font-bold">{track.name}</h2>
      <p>Status: {track.status}</p>
    </div>
  );
}
