'use client';

import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Loader2 } from 'lucide-react';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts';

export default function Dashboard() {
  const { data: budgets, isLoading } = useQuery({ queryKey: ['budgets'], queryFn: api.getBudgets });

  if (isLoading) return <div className="p-8 flex justify-center"><Loader2 className="animate-spin h-8 w-8 text-gray-500" /></div>;

  const currentBudget = budgets?.[0];

  return (
    <main className="p-8 max-w-7xl mx-auto space-y-8">
      <h1 className="text-4xl font-bold text-gray-900">BudgetMaster Dashboard</h1>
      
      {!currentBudget ? (
        <Card>
          <CardContent className="p-6">
            <p>No budgets found. Create one to get started.</p>
          </CardContent>
        </Card>
      ) : (
        <BudgetView budgetId={currentBudget.id} />
      )}
    </main>
  );
}

function BudgetView({ budgetId }: { budgetId: string }) {
  const { data: categories } = useQuery({ queryKey: ['categories', budgetId], queryFn: () => api.getCategories(budgetId) });
  const { data: transactions } = useQuery({ queryKey: ['transactions', budgetId], queryFn: () => api.getTransactions(budgetId) });
  const { data: optimization } = useQuery({ queryKey: ['optimization', budgetId], queryFn: () => api.getOptimization(budgetId) });

  const chartData = categories?.map(c => {
    const spent = transactions?.filter(t => t.categoryId === c.id).reduce((acc, t) => acc + t.amount, 0) || 0;
    return { name: c.name, value: spent, allocated: c.allocatedAmount };
  });

  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#a855f7', '#ec4899'];

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      <Card>
        <CardHeader>
          <CardTitle>Spending Overview</CardTitle>
        </CardHeader>
        <CardContent className="h-64">
          {chartData && chartData.length > 0 ? (
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={chartData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={80} fill="#8884d8" label>
                  {chartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          ) : (
            <p className="text-gray-500">No categories to display.</p>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>AI Optimization</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="whitespace-pre-wrap text-sm text-gray-700">{optimization?.recommendations || 'Loading recommendations...'}</p>
        </CardContent>
      </Card>

      <Card className="md:col-span-2">
        <CardHeader>
          <CardTitle>Categories</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {categories?.map(category => {
              const spent = transactions?.filter(t => t.categoryId === category.id).reduce((acc, t) => acc + t.amount, 0) || 0;
              const percent = Math.min((spent / category.allocatedAmount) * 100, 100);
              return (
                <div key={category.id}>
                  <div className="flex justify-between text-sm mb-1">
                    <span className="font-medium">{category.name}</span>
                    <span>${spent.toFixed(2)} / ${category.allocatedAmount.toFixed(2)}</span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2.5">
                    <div className={`h-2.5 rounded-full ${percent > 90 ? 'bg-red-600' : 'bg-green-600'}`} style={{ width: `${percent}%` }}></div>
                  </div>
                </div>
              );
            })}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
