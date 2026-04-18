'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { useForm } from 'react-hook-form';

export default function IrregularExpensesPage() {
  const { data: expenses, isLoading } = useQuery({ queryKey: ['irregular-expenses'], queryFn: () => (api as any).getIrregularExpenses?.() || Promise.resolve([]) });
  
  const { register, handleSubmit, reset } = useForm();
  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: (data: any) => (api as any).createIrregularExpense?.({ ...data, amount: Number(data.amount) }) || Promise.resolve(),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['irregular-expenses'] });
      reset();
    }
  });

  return (
    <main className="p-8 max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
      <div className="md:col-span-2 space-y-6">
        <h1 className="text-4xl font-bold text-gray-900">Irregular Expenses Planner</h1>
        <Card>
          <CardHeader><CardTitle>Upcoming Expenses</CardTitle></CardHeader>
          <CardContent>
            {isLoading ? <p>Loading...</p> : (
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="border-b">
                    <th className="p-2">Name</th>
                    <th className="p-2">Amount</th>
                    <th className="p-2">Due Date</th>
                    <th className="p-2">Frequency</th>
                  </tr>
                </thead>
                <tbody>
                  {expenses?.map((e: any) => (
                    <tr key={e.id} className="border-b">
                      <td className="p-2">{e.name}</td>
                      <td className="p-2">${e.amount.toFixed(2)}</td>
                      <td className="p-2">{e.dueDate}</td>
                      <td className="p-2">{e.frequency}</td>
                    </tr>
                  ))}
                  {(!expenses || expenses.length === 0) && <tr><td colSpan={4} className="p-2 text-center text-gray-500">No irregular expenses scheduled.</td></tr>}
                </tbody>
              </table>
            )}
          </CardContent>
        </Card>
      </div>

      <div className="space-y-6">
        <Card>
          <CardHeader><CardTitle>Schedule Expense</CardTitle></CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
              <div>
                <label className="block text-sm font-medium">Name</label>
                <input {...register('name', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Amount</label>
                <input type="number" step="0.01" {...register('amount', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Due Date</label>
                <input type="date" {...register('dueDate', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Frequency</label>
                <select {...register('frequency')} className="mt-1 block w-full rounded border p-2">
                  <option value="ANNUAL">Annual</option>
                  <option value="QUARTERLY">Quarterly</option>
                  <option value="ONCE">Once</option>
                </select>
              </div>
              <button type="submit" disabled={createMutation.isPending} className="w-full bg-blue-600 text-white py-2 rounded">Schedule</button>
            </form>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
