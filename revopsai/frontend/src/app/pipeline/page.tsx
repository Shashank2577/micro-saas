"use client";

import { useState, useEffect } from 'react';
import axios from 'axios';

export default function PipelinePage() {
  const [metrics, setMetrics] = useState([]);

  useEffect(() => {
    axios.get('/api/v1/pipeline-metrics', {
      headers: { 'X-Tenant-Id': '123e4567-e89b-12d3-a456-426614174000' }
    }).then(res => setMetrics(res.data)).catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Pipeline Velocity & Coverage</h1>
      <pre>{JSON.stringify(metrics, null, 2)}</pre>
    </div>
  );
}
