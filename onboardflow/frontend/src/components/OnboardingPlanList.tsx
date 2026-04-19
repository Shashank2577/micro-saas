"use client";
import { useEffect, useState } from 'react';
type OnboardingPlan = { id: string; name: string; status: string; };
export default function OnboardingPlanList() {
  const [plans, setPlans] = useState<OnboardingPlan[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    fetch('/api/v1/onboarding/onboarding-plans', { headers: { 'X-Tenant-Id': '00000000-0000-0000-0000-000000000001' } })
      .then(res => { if (!res.ok) throw new Error('Failed'); return res.json(); })
      .then(data => { setPlans(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;
  return <div data-testid="onboarding-plan-list"><ul>{plans.map(p => <li key={p.id}>{p.name} - {p.status}</li>)}</ul></div>;
}
