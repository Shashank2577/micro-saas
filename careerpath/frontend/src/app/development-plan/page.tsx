'use client';

import React, { useState } from 'react';
import { api } from '@/lib/api';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

const MOCK_EMP_ID = '00000000-0000-0000-0000-000000000001';

export default function DevelopmentPlanPage() {
  const queryClient = useQueryClient();
  const [targetRoleId, setTargetRoleId] = useState('');

  const { data: roles } = useQuery({
    queryKey: ['roles'],
    queryFn: () => api.getRoles(),
  });

  const { data: plan, isLoading } = useQuery({
    queryKey: ['development-plan', MOCK_EMP_ID],
    queryFn: () => api.fetchWithTenant(`/employees/${MOCK_EMP_ID}/development-plan`),
  });

  const generatePlanMutation = useMutation({
    mutationFn: (roleId: string) => api.fetchWithTenant(`/employees/${MOCK_EMP_ID}/development-plan`, {
      method: 'POST',
      body: JSON.stringify({ targetRoleId: roleId })
    }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['development-plan', MOCK_EMP_ID] });
    }
  });

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-6">Development Plan</h1>
      
      {!plan || plan.length === 0 ? (
        <div className="mb-6 bg-white p-6 border rounded shadow-sm">
          <h2 className="text-xl font-semibold mb-4">Generate a new Development Plan</h2>
          <div className="flex gap-4">
            <select 
              className="p-2 border rounded flex-1"
              value={targetRoleId}
              onChange={(e) => setTargetRoleId(e.target.value)}
            >
              <option value="">Select Target Role</option>
              {roles?.map((r: any) => (
                <option key={r.id} value={r.id}>{r.title}</option>
              ))}
            </select>
            <button 
              className="bg-blue-600 text-white px-4 py-2 rounded"
              onClick={() => generatePlanMutation.mutate(targetRoleId)}
              disabled={generatePlanMutation.isPending || !targetRoleId}
            >
              {generatePlanMutation.isPending ? 'Generating...' : 'Generate Plan'}
            </button>
          </div>
        </div>
      ) : (
        <div className="bg-white p-6 border rounded shadow-sm">
          <h2 className="text-xl font-semibold mb-4">Your Current Development Plan</h2>
          <p className="mb-4">Status: <span className="font-bold">{plan[0]?.status}</span></p>
          <div className="whitespace-pre-wrap bg-gray-50 p-4 border rounded">
            {plan[0]?.milestonesJson}
          </div>
        </div>
      )}
    </div>
  );
}
