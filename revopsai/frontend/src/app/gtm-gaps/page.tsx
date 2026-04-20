"use client";

import { useState, useEffect } from 'react';
import axios from 'axios';

export default function GtmGapsPage() {
  const [gaps, setGaps] = useState([]);

  useEffect(() => {
    axios.get('/api/v1/gtm-gaps', {
      headers: { 'X-Tenant-Id': '123e4567-e89b-12d3-a456-426614174000' }
    }).then(res => setGaps(res.data)).catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">GTM Execution Gaps</h1>
      <pre>{JSON.stringify(gaps, null, 2)}</pre>
    </div>
  );
}
