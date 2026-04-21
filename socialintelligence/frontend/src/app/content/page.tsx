"use client";
import { useEffect, useState } from 'react';

export default function Content() {
  const [posts, setPosts] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/content/top?limit=10', { headers: { 'X-Tenant-ID': '123e4567-e89b-12d3-a456-426614174000' } })
      .then(res => res.json())
      .then(setPosts)
      .catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Top Content</h1>
      <div className="grid grid-cols-2 gap-4">
        {posts.map(p => (
          <div key={p.id} className="bg-white p-4 rounded shadow">
            <p className="font-bold">{p.caption}</p>
            <p className="text-sm text-gray-500">Likes: {p.likes} | Comments: {p.comments}</p>
          </div>
        ))}
        {posts.length === 0 && <p>No top content available.</p>}
      </div>
    </div>
  );
}
