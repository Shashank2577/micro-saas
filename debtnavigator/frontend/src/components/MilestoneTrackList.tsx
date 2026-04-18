"use client";

import React, { useEffect, useState } from "react";
import { apiFetch } from "@/lib/api";

export default function MilestoneTrackList() {
  const [tracks, setTracks] = useState<any[]>([]);

  useEffect(() => {
    apiFetch("/api/v1/debt/milestone-tracks").then((data) => {
      setTracks(data || []);
    });
  }, []);

  return (
    <div>
      {tracks.length === 0 ? (
        <p>No tracks found.</p>
      ) : (
        <ul data-testid="milestone-list">
          {tracks.map((t: any) => (
            <li key={t.id} className="border p-4 mb-2 rounded">
              {t.name} - {t.status}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
