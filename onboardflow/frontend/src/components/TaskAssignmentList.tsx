"use client";
import { useEffect, useState } from 'react';
type TaskAssignment = { id: string; name: string; status: string; };
export default function TaskAssignmentList() {
  const [items, setItems] = useState<TaskAssignment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    fetch('/api/v1/onboarding/task-assignments', { headers: { 'X-Tenant-Id': '00000000-0000-0000-0000-000000000001' } })
      .then(res => { if (!res.ok) throw new Error('Failed'); return res.json(); })
      .then(data => { setItems(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;
  return <div data-testid="task-assignment-list"><ul>{items.map(p => <li key={p.id}>{p.name} - {p.status}</li>)}</ul></div>;
}
