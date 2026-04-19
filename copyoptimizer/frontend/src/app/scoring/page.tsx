'use client';
import { useState, useEffect } from 'react';
import { Table } from '@/components/Table';
import { fetchAPI } from '@/lib/api';

export default function ScoringPage() {
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAPI<any[]>('/prediction-scores')
      .then(setData)
      .catch(() => setData([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">Prediction Scores</h2>
      {loading ? <p>Loading...</p> : (
        <Table data={data} columns={[{ key: 'name', label: 'Name' }, { key: 'status', label: 'Status' }, { key: 'createdAt', label: 'Created' }]} />
      )}
    </div>
  );
}
