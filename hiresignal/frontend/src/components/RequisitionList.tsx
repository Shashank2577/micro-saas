'use client';
import { useState, useEffect } from 'react';

export default function RequisitionList() {
  const [items, setItems] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/v1/hiring/requisitions', {
      headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }
    })
      .then(res => res.json())
      .then(data => setItems(Array.isArray(data) ? data : []))
      .catch(err => console.error(err));
  }, []);

  return (
    <div data-testid="requisition-list">
      {items.length === 0 ? <p>No requisitions found.</p> : (
        <ul>
          {items.map((item: any) => (
            <li key={item.id}>{item.name} - {item.status}</li>
          ))}
        </ul>
      )}
    </div>
  );
}
