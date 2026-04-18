'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { useForm } from 'react-hook-form';
import { useState } from 'react';

export default function TransactionsPage() {
  const { data: budgets } = useQuery({ queryKey: ['budgets'], queryFn: api.getBudgets });
  const budgetId = budgets?.[0]?.id;

  const { data: transactions, isLoading } = useQuery({
    queryKey: ['transactions', budgetId],
    queryFn: () => api.getTransactions(budgetId!),
    enabled: !!budgetId
  });
  
  const { data: categories } = useQuery({
    queryKey: ['categories', budgetId],
    queryFn: () => api.getCategories(budgetId!),
    enabled: !!budgetId
  });

  const { register, handleSubmit, reset, setValue } = useForm();
  const queryClient = useQueryClient();
  const [categorizeLoading, setCategorizeLoading] = useState(false);

  const createMutation = useMutation({
    mutationFn: (data: any) => api.recordTransaction({
      ...data,
      budgetId,
      amount: Number(data.amount),
      categoryId: data.categoryId || null,
    }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions', budgetId] });
      reset();
    }
  });

  const handleCategorize = async (desc: string, amt: string) => {
    setCategorizeLoading(true);
    try {
      const res = await api.categorizeTransaction({ description: desc, amount: Number(amt) });
      const foundCat = categories?.find(c => c.name.toLowerCase() === res.category.toLowerCase());
      if (foundCat) {
        setValue('categoryId', foundCat.id);
        alert(`AI Suggested Category: ${foundCat.name}`);
      } else {
        alert(`AI Suggested: ${res.category}, but it's not in your budget.`);
      }
    } catch (e) {
      console.error(e);
    }
    setCategorizeLoading(false);
  };

  if (!budgetId) return <div className="p-8">No budget found. Please create one.</div>;

  return (
    <main className="p-8 max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
      <div className="md:col-span-2 space-y-6">
        <h1 className="text-4xl font-bold text-gray-900">Transactions</h1>
        <Card>
          <CardHeader><CardTitle>Recent Transactions</CardTitle></CardHeader>
          <CardContent>
            {isLoading ? <p>Loading...</p> : (
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="border-b">
                    <th className="p-2">Date</th>
                    <th className="p-2">Description</th>
                    <th className="p-2">Category</th>
                    <th className="p-2">Amount</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions?.map(t => (
                    <tr key={t.id} className="border-b">
                      <td className="p-2">{t.date}</td>
                      <td className="p-2">{t.description}</td>
                      <td className="p-2">{categories?.find(c => c.id === t.categoryId)?.name || 'Uncategorized'}</td>
                      <td className="p-2">${t.amount.toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </CardContent>
        </Card>
      </div>

      <div className="space-y-6">
        <Card>
          <CardHeader><CardTitle>Record Transaction</CardTitle></CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
              <div>
                <label className="block text-sm font-medium">Date</label>
                <input type="date" {...register('date', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Description</label>
                <input id="descInput" {...register('description', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Amount</label>
                <input id="amtInput" type="number" step="0.01" {...register('amount', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Category</label>
                <div className="flex gap-2">
                  <select {...register('categoryId')} className="mt-1 block w-full rounded border p-2">
                    <option value="">-- Uncategorized --</option>
                    {categories?.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                  </select>
                  <button type="button" onClick={() => handleCategorize((document.getElementById('descInput') as HTMLInputElement).value, (document.getElementById('amtInput') as HTMLInputElement).value)} disabled={categorizeLoading} className="mt-1 bg-gray-200 px-2 rounded text-sm whitespace-nowrap">
                    {categorizeLoading ? '...' : 'AI Guess'}
                  </button>
                </div>
              </div>
              <button type="submit" disabled={createMutation.isPending} className="w-full bg-blue-600 text-white py-2 rounded">Save</button>
            </form>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
