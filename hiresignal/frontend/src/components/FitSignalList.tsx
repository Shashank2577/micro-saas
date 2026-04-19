'use client';
import { useState, useEffect } from 'react';

export default function FitSignalList() {
  const [items, setItems] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/v1/hiring/fit-signals', {
      headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }
    })
      .then(res => res.json())
      .then(data => setItems(Array.isArray(data) ? data : []))
      .catch(err => console.error(err));
  }, []);

  return (
    <div data-testid="fit-signal-list">
      {items.length === 0 ? <p>No fit signals found.</p> : (
        <ul>
          {items.map((item: any) => (
            <li key={item.id}>{item.name} - {item.status}</li>
          ))}
        </ul>
      )}
    </div>
  );
}
