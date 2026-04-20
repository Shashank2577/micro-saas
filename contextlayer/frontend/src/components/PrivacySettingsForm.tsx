"use client";
import React, { useState } from 'react';

export function PrivacySettingsForm({ customerId }: { customerId: string }) {
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');

  const submitConsent = async (type: string, granted: boolean) => {
    setLoading(true);
    setSuccess('');
    try {
      const res = await fetch(`/api/customers/${customerId}/consent`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'X-App-Id': 'contextlayer-dashboard' },
        body: JSON.stringify({ consentType: type, granted }),
      });
      if (!res.ok) throw new Error('Failed to record consent');
      setSuccess('Consent recorded successfully.');
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-4 shadow rounded-lg border border-gray-200 mt-4">
      <h2 className="text-xl font-semibold mb-4">Privacy Controls</h2>
      <div className="flex gap-4">
        <button
          onClick={() => submitConsent('marketing', true)}
          disabled={loading}
          className="bg-green-500 text-white px-4 py-2 rounded shadow hover:bg-green-600 disabled:opacity-50"
        >
          Opt-in to Marketing
        </button>
        <button
          onClick={() => submitConsent('marketing', false)}
          disabled={loading}
          className="bg-red-500 text-white px-4 py-2 rounded shadow hover:bg-red-600 disabled:opacity-50"
        >
          Opt-out of Marketing
        </button>
      </div>
      {success && <p className="mt-4 text-sm text-green-600">{success}</p>}
    </div>
  );
}
