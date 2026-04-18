'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { useForm } from 'react-hook-form';

export default function CategoriesPage() {
  const { data: budgets } = useQuery({ queryKey: ['budgets'], queryFn: api.getBudgets });
  const budgetId = budgets?.[0]?.id;

  const { data: categories, isLoading } = useQuery({
    queryKey: ['categories', budgetId],
    queryFn: () => api.getCategories(budgetId!),
    enabled: !!budgetId
  });

  const { register, handleSubmit, reset } = useForm();
  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: (data: any) => api.createCategory(budgetId!, { ...data, allocatedAmount: Number(data.allocatedAmount), carryover: data.carryover === 'true' }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories', budgetId] });
      reset();
    }
  });

  if (!budgetId) return <div className="p-8">No budget found. Please create one.</div>;

  return (
    <main className="p-8 max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
      <div className="md:col-span-2 space-y-6">
        <h1 className="text-4xl font-bold text-gray-900">Categories</h1>
        <Card>
          <CardHeader><CardTitle>Budget Envelope Allocations</CardTitle></CardHeader>
          <CardContent>
            {isLoading ? <p>Loading...</p> : (
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="border-b">
                    <th className="p-2">Name</th>
                    <th className="p-2">Type</th>
                    <th className="p-2">Allocated</th>
                    <th className="p-2">Carryover</th>
                  </tr>
                </thead>
                <tbody>
                  {categories?.map(c => (
                    <tr key={c.id} className="border-b">
                      <td className="p-2">{c.name}</td>
                      <td className="p-2">{c.type}</td>
                      <td className="p-2">${c.allocatedAmount.toFixed(2)}</td>
                      <td className="p-2">{c.carryover ? 'Yes' : 'No'}</td>
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
          <CardHeader><CardTitle>Add Category</CardTitle></CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
              <div>
                <label className="block text-sm font-medium">Name</label>
                <input {...register('name', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Allocated Amount</label>
                <input type="number" step="0.01" {...register('allocatedAmount', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Type</label>
                <select {...register('type')} className="mt-1 block w-full rounded border p-2">
                  <option value="EXPENSE">Expense</option>
                  <option value="SAVINGS">Savings</option>
                  <option value="IRREGULAR">Irregular</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium">Carryover (Rollover)</label>
                <select {...register('carryover')} className="mt-1 block w-full rounded border p-2">
                  <option value="false">No</option>
                  <option value="true">Yes</option>
                </select>
              </div>
              <button type="submit" disabled={createMutation.isPending} className="w-full bg-blue-600 text-white py-2 rounded">Add</button>
            </form>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
