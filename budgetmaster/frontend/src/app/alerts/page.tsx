'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { useForm } from 'react-hook-form';

export default function AlertsPage() {
  const { data: budgets } = useQuery({ queryKey: ['budgets'], queryFn: api.getBudgets });
  const budgetId = budgets?.[0]?.id;

  const { data: categories } = useQuery({
    queryKey: ['categories', budgetId],
    queryFn: () => api.getCategories(budgetId!),
    enabled: !!budgetId
  });

  const { data: alerts, isLoading } = useQuery({ queryKey: ['alerts'], queryFn: api.getAlerts });
  
  const { register, handleSubmit, reset } = useForm();
  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: (data: any) => api.createAlert({
      ...data,
      thresholdPercent: data.thresholdPercent ? Number(data.thresholdPercent) : undefined,
      thresholdAmount: data.thresholdAmount ? Number(data.thresholdAmount) : undefined
    }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['alerts'] });
      reset();
    }
  });

  return (
    <main className="p-8 max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
      <div className="md:col-span-2 space-y-6">
        <h1 className="text-4xl font-bold text-gray-900">Spending Alerts</h1>
        <Card>
          <CardHeader><CardTitle>Configured Alerts</CardTitle></CardHeader>
          <CardContent>
            {isLoading ? <p>Loading...</p> : (
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="border-b">
                    <th className="p-2">Category</th>
                    <th className="p-2">Threshold (%)</th>
                    <th className="p-2">Threshold ($)</th>
                    <th className="p-2">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {alerts?.map((a: any) => (
                    <tr key={a.id} className="border-b">
                      <td className="p-2">{categories?.find(c => c.id === a.categoryId)?.name || 'Unknown'}</td>
                      <td className="p-2">{a.thresholdPercent ? `${a.thresholdPercent}%` : '-'}</td>
                      <td className="p-2">{a.thresholdAmount ? `$${a.thresholdAmount.toFixed(2)}` : '-'}</td>
                      <td className="p-2">
                        <span className={`px-2 py-1 rounded text-xs text-white ${a.triggered ? 'bg-red-500' : 'bg-green-500'}`}>
                          {a.triggered ? 'TRIGGERED' : 'OK'}
                        </span>
                      </td>
                    </tr>
                  ))}
                  {(!alerts || alerts.length === 0) && <tr><td colSpan={4} className="p-2 text-center text-gray-500">No alerts configured.</td></tr>}
                </tbody>
              </table>
            )}
          </CardContent>
        </Card>
      </div>

      <div className="space-y-6">
        <Card>
          <CardHeader><CardTitle>Create Alert</CardTitle></CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
              <div>
                <label className="block text-sm font-medium">Category</label>
                <select {...register('categoryId', { required: true })} className="mt-1 block w-full rounded border p-2">
                  <option value="">-- Select Category --</option>
                  {categories?.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium">Threshold (%)</label>
                <input type="number" step="0.01" {...register('thresholdPercent')} className="mt-1 block w-full rounded border p-2" placeholder="e.g. 75" />
              </div>
              <div className="text-center text-sm text-gray-500">OR</div>
              <div>
                <label className="block text-sm font-medium">Threshold Amount ($)</label>
                <input type="number" step="0.01" {...register('thresholdAmount')} className="mt-1 block w-full rounded border p-2" />
              </div>
              <button type="submit" disabled={createMutation.isPending} className="w-full bg-blue-600 text-white py-2 rounded">Create Alert</button>
            </form>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
