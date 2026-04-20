"use client";

import { useState, useEffect } from 'react';
import axios from 'axios';

export default function ForecastPage() {
  const [acc, setAcc] = useState([]);

  useEffect(() => {
    axios.get('/api/v1/forecast-accuracy', {
      headers: { 'X-Tenant-Id': '123e4567-e89b-12d3-a456-426614174000' }
    }).then(res => setAcc(res.data)).catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Forecast Accuracy</h1>
      <pre>{JSON.stringify(acc, null, 2)}</pre>
    </div>
  );
}
