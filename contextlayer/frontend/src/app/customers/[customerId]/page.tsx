import React from 'react';
import { ContextDashboard } from '../../../components/ContextDashboard';
import { PrivacySettingsForm } from '../../../components/PrivacySettingsForm';
import { ContextTreeViewer } from '../../../components/ContextTreeViewer';

export default function CustomerDetailPage({ params }: { params: { customerId: string } }) {
  return (
    <div className="p-6 max-w-5xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold mb-4">Customer Context: {params.customerId}</h1>
      <ContextTreeViewer customerId={params.customerId} />
      <ContextDashboard customerId={params.customerId} />
      <PrivacySettingsForm customerId={params.customerId} />
    </div>
  );
}
