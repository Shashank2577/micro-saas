'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { api } from '@/lib/api';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { useForm } from 'react-hook-form';

export default function FamilyPage() {
  const { data: members, isLoading } = useQuery({ queryKey: ['family-members'], queryFn: () => (api as any).getFamilyMembers?.() || Promise.resolve([]) });
  
  const { register, handleSubmit, reset } = useForm();
  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: (data: any) => (api as any).createFamilyMember?.({ ...data, individualAllowance: Number(data.individualAllowance) }) || Promise.resolve(),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['family-members'] });
      reset();
    }
  });

  return (
    <main className="p-8 max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
      <div className="md:col-span-2 space-y-6">
        <h1 className="text-4xl font-bold text-gray-900">Family Collaboration</h1>
        <Card>
          <CardHeader><CardTitle>Members & Allowances</CardTitle></CardHeader>
          <CardContent>
            {isLoading ? <p>Loading...</p> : (
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="border-b">
                    <th className="p-2">Name</th>
                    <th className="p-2">Role</th>
                    <th className="p-2">Allowance</th>
                  </tr>
                </thead>
                <tbody>
                  {members?.map((m: any) => (
                    <tr key={m.id} className="border-b">
                      <td className="p-2">{m.name}</td>
                      <td className="p-2">{m.role}</td>
                      <td className="p-2">${m.individualAllowance?.toFixed(2) || '0.00'}</td>
                    </tr>
                  ))}
                  {(!members || members.length === 0) && <tr><td colSpan={3} className="p-2 text-center text-gray-500">No family members added.</td></tr>}
                </tbody>
              </table>
            )}
          </CardContent>
        </Card>
      </div>

      <div className="space-y-6">
        <Card>
          <CardHeader><CardTitle>Add Member</CardTitle></CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit((d) => createMutation.mutate(d))} className="space-y-4">
              <div>
                <label className="block text-sm font-medium">Name</label>
                <input {...register('name', { required: true })} className="mt-1 block w-full rounded border p-2" />
              </div>
              <div>
                <label className="block text-sm font-medium">Role</label>
                <select {...register('role')} className="mt-1 block w-full rounded border p-2">
                  <option value="SPOUSE">Spouse</option>
                  <option value="CHILD">Child</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium">Individual Allowance</label>
                <input type="number" step="0.01" {...register('individualAllowance')} className="mt-1 block w-full rounded border p-2" />
              </div>
              <button type="submit" disabled={createMutation.isPending} className="w-full bg-blue-600 text-white py-2 rounded">Add Member</button>
            </form>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}
