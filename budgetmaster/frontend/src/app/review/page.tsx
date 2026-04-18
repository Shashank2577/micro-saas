'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import Link from 'next/link';

export default function ReviewPage() {
  const { data: budgets } = useQuery({ queryKey: ['budgets'], queryFn: api.getBudgets });
  const budgetId = budgets?.[0]?.id;

  const { data: transactions } = useQuery({
    queryKey: ['transactions', budgetId],
    queryFn: () => api.getTransactions(budgetId!),
    enabled: !!budgetId
  });

  const uncategorized = transactions?.filter(t => !t.categoryId) || [];

  return (
    <main className="p-8 max-w-4xl mx-auto space-y-8">
      <h1 className="text-4xl font-bold text-gray-900">Monthly Review Checklist</h1>
      
      <Card>
        <CardHeader><CardTitle>Tasks</CardTitle></CardHeader>
        <CardContent className="space-y-4">
          <div className="flex items-center gap-4">
            <input type="checkbox" className="h-5 w-5" checked={uncategorized.length === 0} readOnly />
            <span className={uncategorized.length === 0 ? "line-through text-gray-500" : "font-medium"}>
              Categorize all transactions ({uncategorized.length} remaining)
            </span>
            {uncategorized.length > 0 && <Link href="/transactions" className="text-blue-600 text-sm hover:underline">Go to Transactions</Link>}
          </div>
          <div className="flex items-center gap-4">
            <input type="checkbox" className="h-5 w-5" />
            <span className="font-medium">Review AI optimization recommendations</span>
            <Link href="/" className="text-blue-600 text-sm hover:underline">View Dashboard</Link>
          </div>
          <div className="flex items-center gap-4">
            <input type="checkbox" className="h-5 w-5" />
            <span className="font-medium">Verify rolling budget carryovers</span>
            <Link href="/categories" className="text-blue-600 text-sm hover:underline">View Categories</Link>
          </div>
        </CardContent>
      </Card>
    </main>
  );
}
