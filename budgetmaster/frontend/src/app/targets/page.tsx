'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';

export default function TargetsPage() {
  const { data: targets, isLoading } = useQuery({ queryKey: ['targets'], queryFn: api.getTargets });

  return (
    <main className="p-8 max-w-4xl mx-auto space-y-8">
      <h1 className="text-4xl font-bold text-gray-900">Savings Targets</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {isLoading ? <p>Loading...</p> : targets?.map(target => {
          const percent = Math.min((target.currentAmount / target.targetAmount) * 100, 100);
          return (
            <Card key={target.id}>
              <CardHeader><CardTitle>{target.name}</CardTitle></CardHeader>
              <CardContent>
                <div className="flex justify-between mb-2">
                  <span>${target.currentAmount.toFixed(2)}</span>
                  <span className="text-gray-500">of ${target.targetAmount.toFixed(2)}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-4">
                  <div className="bg-blue-600 h-4 rounded-full" style={{ width: `${percent}%` }}></div>
                </div>
                {target.deadline && <p className="text-sm text-gray-500 mt-4">Deadline: {target.deadline}</p>}
              </CardContent>
            </Card>
          );
        })}
        {targets?.length === 0 && <p>No targets created yet.</p>}
      </div>
    </main>
  );
}
