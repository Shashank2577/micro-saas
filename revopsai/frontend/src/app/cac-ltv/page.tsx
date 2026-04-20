"use client";

import { useState, useEffect } from 'react';
import axios from 'axios';

export default function CacLtvPage() {
  const [cac, setCac] = useState([]);
  const [ltv, setLtv] = useState([]);

  useEffect(() => {
    const fetch = async () => {
      try {
        const h = { 'X-Tenant-Id': '123e4567-e89b-12d3-a456-426614174000' };
        const [c, l] = await Promise.all([
          axios.get('/api/v1/cac-calculations', { headers: h }),
          axios.get('/api/v1/ltv-models', { headers: h })
        ]);
        setCac(c.data);
        setLtv(l.data);
      } catch (e) {
        console.error(e);
      }
    };
    fetch();
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">CAC & LTV Analysis</h1>
      <h2 className="text-xl mt-4">CAC</h2>
      <pre>{JSON.stringify(cac, null, 2)}</pre>
      <h2 className="text-xl mt-4">LTV</h2>
      <pre>{JSON.stringify(ltv, null, 2)}</pre>
    </div>
  );
}
