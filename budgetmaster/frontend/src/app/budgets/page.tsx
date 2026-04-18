'use client';

import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { useState } from 'react';

export default function BudgetSetupPage() {
  const { register, handleSubmit, reset, setValue } = useForm();
  const queryClient = useQueryClient();

  const [selectedTemplate, setSelectedTemplate] = useState('');

  const mutation = useMutation({
    mutationFn: (data: any) => api.createBudget({ ...data, monthlyIncome: Number(data.monthlyIncome), month: Number(data.month), year: Number(data.year), rollingMode: data.rollingMode === 'true' }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['budgets'] });
      reset();
      alert('Budget created successfully');
    },
  });

  const applyTemplate = (template: string) => {
    setSelectedTemplate(template);
    setValue('type', 'INCOME_BASED');
    // Basic auto-fill simulation for templates
    if (template === 'minimalist') {
      setValue('name', 'Minimalist Budget');
      setValue('monthlyIncome', '3000');
    } else if (template === 'comfort') {
      setValue('name', 'Comfort Budget');
      setValue('monthlyIncome', '6000');
    } else if (template === 'luxury') {
      setValue('name', 'Luxury Budget');
      setValue('monthlyIncome', '12000');
    }
  };

  return (
    <main className="p-8 max-w-3xl mx-auto space-y-8">
      <h1 className="text-4xl font-bold text-gray-900">Setup Budget</h1>
      <Card>
        <CardHeader>
          <CardTitle>Create New Budget</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="mb-6">
            <label className="block text-sm font-medium mb-2">Use a Template</label>
            <div className="flex gap-4">
              <button type="button" onClick={() => applyTemplate('minimalist')} className={`px-4 py-2 border rounded ${selectedTemplate === 'minimalist' ? 'bg-blue-100 border-blue-500' : ''}`}>Minimalist</button>
              <button type="button" onClick={() => applyTemplate('comfort')} className={`px-4 py-2 border rounded ${selectedTemplate === 'comfort' ? 'bg-blue-100 border-blue-500' : ''}`}>Comfort</button>
              <button type="button" onClick={() => applyTemplate('luxury')} className={`px-4 py-2 border rounded ${selectedTemplate === 'luxury' ? 'bg-blue-100 border-blue-500' : ''}`}>Luxury</button>
            </div>
          </div>
          <form onSubmit={handleSubmit((d) => mutation.mutate(d))} className="space-y-4">
            <div>
              <label className="block text-sm font-medium">Name</label>
              <input {...register('name', { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" />
            </div>
            <div>
              <label className="block text-sm font-medium">Monthly Income</label>
              <input type="number" step="0.01" {...register('monthlyIncome', { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" />
            </div>
            <div>
              <label className="block text-sm font-medium">Type</label>
              <select {...register('type', { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2">
                <option value="FIXED">Fixed</option>
                <option value="INCOME_BASED">Income Based</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium">Rolling Mode</label>
              <select {...register('rollingMode')} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2">
                <option value="false">Disabled</option>
                <option value="true">Enabled (12-Month Forecast)</option>
              </select>
            </div>
            <div className="flex gap-4">
              <div className="flex-1">
                <label className="block text-sm font-medium">Month</label>
                <input type="number" {...register('month', { required: true, min: 1, max: 12 })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" />
              </div>
              <div className="flex-1">
                <label className="block text-sm font-medium">Year</label>
                <input type="number" {...register('year', { required: true })} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm border p-2" />
              </div>
            </div>
            <button type="submit" disabled={mutation.isPending} className="bg-blue-600 text-white px-4 py-2 rounded shadow">
              {mutation.isPending ? 'Saving...' : 'Save Budget'}
            </button>
          </form>
        </CardContent>
      </Card>
    </main>
  );
}
