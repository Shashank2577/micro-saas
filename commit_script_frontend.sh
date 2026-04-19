#!/bin/bash
git checkout feat/onboardflow-implementation

# Phase 2.2a
npm --prefix onboardflow/frontend install vitest @testing-library/react @testing-library/dom jsdom @vitejs/plugin-react @testing-library/jest-dom lucide-react --save-dev
cat << 'EOF2' > onboardflow/frontend/vitest.config.ts
import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  test: {
    environment: 'jsdom',
    setupFiles: ['./src/setupTests.ts'],
    globals: true,
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
})
EOF2
cat << 'EOF2' > onboardflow/frontend/src/setupTests.ts
import '@testing-library/jest-dom'
EOF2

# Phase 2.2b
mkdir -p onboardflow/frontend/src/app/onboarding-plans
mkdir -p onboardflow/frontend/src/app/milestone-checklists
mkdir -p onboardflow/frontend/src/app/task-assignments
mkdir -p onboardflow/frontend/src/app/completion-signals
mkdir -p onboardflow/frontend/src/app/escalations
mkdir -p onboardflow/frontend/src/app/experience-scores
cat << 'EOF2' > onboardflow/frontend/src/app/onboarding-plans/page.tsx
import OnboardingPlanList from '@/components/OnboardingPlanList';
export default function OnboardingPlansPage() { return <div className="container mx-auto py-8"><h1 className="text-3xl font-bold mb-6">Onboarding Plans</h1><OnboardingPlanList /></div>; }
EOF2
cat << 'EOF2' > onboardflow/frontend/src/app/milestone-checklists/page.tsx
import MilestoneChecklistList from '@/components/MilestoneChecklistList';
export default function MilestoneChecklistsPage() { return <div className="container mx-auto py-8"><h1 className="text-3xl font-bold mb-6">Milestone Checklists</h1><MilestoneChecklistList /></div>; }
EOF2
cat << 'EOF2' > onboardflow/frontend/src/app/task-assignments/page.tsx
import TaskAssignmentList from '@/components/TaskAssignmentList';
export default function TaskAssignmentsPage() { return <div className="container mx-auto py-8"><h1 className="text-3xl font-bold mb-6">Task Assignments</h1><TaskAssignmentList /></div>; }
EOF2
cat << 'EOF2' > onboardflow/frontend/src/app/completion-signals/page.tsx
import CompletionSignalList from '@/components/CompletionSignalList';
export default function CompletionSignalsPage() { return <div className="container mx-auto py-8"><h1 className="text-3xl font-bold mb-6">Completion Signals</h1><CompletionSignalList /></div>; }
EOF2
cat << 'EOF2' > onboardflow/frontend/src/app/escalations/page.tsx
import EscalationList from '@/components/EscalationList';
export default function EscalationsPage() { return <div className="container mx-auto py-8"><h1 className="text-3xl font-bold mb-6">Escalations</h1><EscalationList /></div>; }
EOF2
cat << 'EOF2' > onboardflow/frontend/src/app/experience-scores/page.tsx
import ExperienceScoreList from '@/components/ExperienceScoreList';
export default function ExperienceScoresPage() { return <div className="container mx-auto py-8"><h1 className="text-3xl font-bold mb-6">Experience Scores</h1><ExperienceScoreList /></div>; }
EOF2

# Phase 2.2c
cat << 'EOF2' > onboardflow/frontend/src/components/OnboardingPlanList.tsx
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
EOF2
cat << 'EOF2' > onboardflow/frontend/src/components/MilestoneChecklistList.tsx
"use client";
import { useEffect, useState } from 'react';
type MilestoneChecklist = { id: string; name: string; status: string; };
export default function MilestoneChecklistList() {
  const [items, setItems] = useState<MilestoneChecklist[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    fetch('/api/v1/onboarding/milestone-checklists', { headers: { 'X-Tenant-Id': '00000000-0000-0000-0000-000000000001' } })
      .then(res => { if (!res.ok) throw new Error('Failed'); return res.json(); })
      .then(data => { setItems(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;
  return <div data-testid="milestone-checklist-list"><ul>{items.map(p => <li key={p.id}>{p.name} - {p.status}</li>)}</ul></div>;
}
EOF2
cat << 'EOF2' > onboardflow/frontend/src/components/TaskAssignmentList.tsx
"use client";
import { useEffect, useState } from 'react';
type TaskAssignment = { id: string; name: string; status: string; };
export default function TaskAssignmentList() {
  const [items, setItems] = useState<TaskAssignment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    fetch('/api/v1/onboarding/task-assignments', { headers: { 'X-Tenant-Id': '00000000-0000-0000-0000-000000000001' } })
      .then(res => { if (!res.ok) throw new Error('Failed'); return res.json(); })
      .then(data => { setItems(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;
  return <div data-testid="task-assignment-list"><ul>{items.map(p => <li key={p.id}>{p.name} - {p.status}</li>)}</ul></div>;
}
EOF2
cat << 'EOF2' > onboardflow/frontend/src/components/CompletionSignalList.tsx
"use client";
import { useEffect, useState } from 'react';
type CompletionSignal = { id: string; name: string; status: string; };
export default function CompletionSignalList() {
  const [items, setItems] = useState<CompletionSignal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    fetch('/api/v1/onboarding/completion-signals', { headers: { 'X-Tenant-Id': '00000000-0000-0000-0000-000000000001' } })
      .then(res => { if (!res.ok) throw new Error('Failed'); return res.json(); })
      .then(data => { setItems(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;
  return <div data-testid="completion-signal-list"><ul>{items.map(p => <li key={p.id}>{p.name} - {p.status}</li>)}</ul></div>;
}
EOF2
cat << 'EOF2' > onboardflow/frontend/src/components/EscalationList.tsx
"use client";
import { useEffect, useState } from 'react';
type Escalation = { id: string; name: string; status: string; };
export default function EscalationList() {
  const [items, setItems] = useState<Escalation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    fetch('/api/v1/onboarding/escalations', { headers: { 'X-Tenant-Id': '00000000-0000-0000-0000-000000000001' } })
      .then(res => { if (!res.ok) throw new Error('Failed'); return res.json(); })
      .then(data => { setItems(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;
  return <div data-testid="escalation-list"><ul>{items.map(p => <li key={p.id}>{p.name} - {p.status}</li>)}</ul></div>;
}
EOF2
cat << 'EOF2' > onboardflow/frontend/src/components/ExperienceScoreList.tsx
"use client";
import { useEffect, useState } from 'react';
type ExperienceScore = { id: string; name: string; status: string; };
export default function ExperienceScoreList() {
  const [items, setItems] = useState<ExperienceScore[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    fetch('/api/v1/onboarding/experience-scores', { headers: { 'X-Tenant-Id': '00000000-0000-0000-0000-000000000001' } })
      .then(res => { if (!res.ok) throw new Error('Failed'); return res.json(); })
      .then(data => { setItems(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);
  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error</div>;
  return <div data-testid="experience-score-list"><ul>{items.map(p => <li key={p.id}>{p.name} - {p.status}</li>)}</ul></div>;
}
EOF2

# Phase 2.2d
cat << 'EOF2' > onboardflow/frontend/src/components/OnboardingPlanList.test.tsx
import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import OnboardingPlanList from './OnboardingPlanList';
describe('OnboardingPlanList', () => {
  it('renders loading state initially', () => {
    vi.spyOn(global, 'fetch').mockImplementation(() => new Promise(() => {}));
    render(<OnboardingPlanList />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });
  it('renders list of plans after fetch', async () => {
    const mockPlans = [{ id: '1', name: 'Plan A', status: 'DRAFT' }];
    vi.spyOn(global, 'fetch').mockResolvedValue({ ok: true, json: () => Promise.resolve(mockPlans) } as Response);
    render(<OnboardingPlanList />);
    await waitFor(() => { expect(screen.getByTestId('onboarding-plan-list')).toBeInTheDocument(); });
    expect(screen.getByText('Plan A - DRAFT')).toBeInTheDocument();
  });
});
EOF2

cat << 'EOF2' > onboardflow/frontend/src/lib/api.ts
const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8125';
export interface EcosystemApp {
  id: string;
  name: string;
  displayName: string;
  description?: string;
  status: 'active' | 'beta' | 'planned';
  baseUrl?: string;
  lastHeartbeatAt?: string;
}
EOF2

cat << 'EOF2' > onboardflow/frontend/package.json
{
  "name": "onboardflow-frontend",
  "version": "0.0.1",
  "private": true,
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "next lint",
    "test": "vitest run"
  },
  "dependencies": {
    "lucide-react": "^0.477.0",
    "next": "14.2.0",
    "react": "^18.3.0",
    "react-dom": "^18.3.0"
  },
  "devDependencies": {
    "@testing-library/dom": "^10.4.1",
    "@testing-library/jest-dom": "^6.9.1",
    "@testing-library/react": "^16.3.2",
    "@types/node": "^20.0.0",
    "@types/react": "^18.3.0",
    "@types/react-dom": "^18.3.0",
    "@vitejs/plugin-react": "^4.3.4",
    "autoprefixer": "^10.4.19",
    "eslint": "^8.57.0",
    "eslint-config-next": "14.2.0",
    "jsdom": "^29.0.2",
    "postcss": "^8.4.38",
    "tailwindcss": "^3.4.3",
    "typescript": "^5.4.5",
    "vitest": "^2.1.8"
  }
}
EOF2
mv onboardflow/frontend/next.config.ts onboardflow/frontend/next.config.mjs

git add onboardflow/frontend/
git commit -m "feat(onboardflow): add frontend dependencies, pages, components, tests"
