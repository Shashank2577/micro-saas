"use client";
import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
import { Survey } from '@/types';
import Link from 'next/link';

export default function SurveysPage() {
  const [surveys, setSurveys] = useState<Survey[]>([]);

  useEffect(() => {
    api.get('/surveys').then(res => setSurveys(res.data)).catch(console.error);
  }, []);

  return (
    <div className="p-8 space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Surveys</h1>
        <Link href="/surveys/create" className="bg-blue-600 text-white px-4 py-2 rounded">
          Create Survey
        </Link>
      </div>
      <div className="space-y-4">
        {surveys.map(s => (
          <div key={s.id} className="p-4 border rounded shadow-sm bg-white">
            <h3 className="text-xl font-bold">{s.title}</h3>
            <p className="text-gray-600">{s.status}</p>
          </div>
        ))}
        {surveys.length === 0 && <p>No surveys found.</p>}
      </div>
    </div>
  );
}
