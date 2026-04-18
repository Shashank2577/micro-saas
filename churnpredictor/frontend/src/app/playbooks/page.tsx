'use client';

import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { api } from '@/lib/api';

export default function PlaybooksPage() {
  const [playbooks, setPlaybooks] = useState<any[]>([]);

  useEffect(() => {
    fetchPlaybooks();
  }, []);

  const fetchPlaybooks = async () => {
    try {
      const res = await api.get('/api/v1/playbooks');
      setPlaybooks(res.data);
    } catch (err) {
      console.error('Failed to fetch playbooks', err);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Intervention Playbooks</h1>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {playbooks.map((p) => (
          <Card key={p.id}>
            <CardHeader>
              <CardTitle>{p.name}</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-gray-500 mb-4">{p.description}</p>
              <div className="space-y-2 text-sm">
                <div className="flex justify-between">
                  <span className="font-semibold">Trigger Risk:</span>
                  <span>{p.triggerRiskSegment}</span>
                </div>
                <div className="flex justify-between">
                  <span className="font-semibold">Action Type:</span>
                  <span>{p.actionType}</span>
                </div>
                <div className="flex justify-between">
                  <span className="font-semibold">Status:</span>
                  <span className={p.active ? "text-green-600 font-semibold" : "text-gray-500"}>
                    {p.active ? "Active" : "Inactive"}
                  </span>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
        {playbooks.length === 0 && (
          <div className="col-span-full text-center py-10 text-gray-500 bg-white border rounded-lg">
            No playbooks found
          </div>
        )}
      </div>
    </div>
  );
}
