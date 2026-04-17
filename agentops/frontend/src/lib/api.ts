export interface AgentRun {
  id: string;
  tenantId: string;
  agentId: string;
  workflowId: string;
  startedAt: string;
  completedAt?: string;
  status: string;
  tokenCost: number;
}

export interface AgentStep {
  id: string;
  run: AgentRun;
  stepType: string;
  input: string;
  output: string;
  durationMs: number;
  createdAt: string;
}

export interface HumanEscalation {
  id: string;
  run: AgentRun;
  reason: string;
  context: string;
  assignedTo?: string;
  resolvedAt?: string;
  resolution?: string;
  status: string;
  createdAt: string;
}

export interface AgentHealthMetric {
  id: string;
  agentId: string;
  periodStart: string;
  periodEnd: string;
  successRate: number;
  avgCost: number;
  avgLatencyMs: number;
  escalationRate: number;
}

export interface CostAllocation {
  id: string;
  agentId: string;
  teamId: string;
  productFeature: string;
  period: string;
  totalCost: number;
}

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
const DEFAULT_TENANT_ID = '00000000-0000-0000-0000-000000000000';

const fetchApi = async (endpoint: string, options: RequestInit = {}) => {
  const res = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      'X-Tenant-ID': DEFAULT_TENANT_ID,
      ...options.headers,
    },
  });
  if (!res.ok) {
    throw new Error(`API error: ${res.statusText}`);
  }
  return res.json();
};

export const getRuns = (): Promise<AgentRun[]> => fetchApi('/api/runs');
export const getRun = (id: string): Promise<AgentRun> => fetchApi(`/api/runs/${id}`);
export const getRunSteps = (id: string): Promise<AgentStep[]> => fetchApi(`/api/runs/${id}/steps`);
export const getPendingEscalations = (): Promise<HumanEscalation[]> => fetchApi('/api/escalations');
export const resolveEscalation = (id: string, resolution: string): Promise<HumanEscalation> => 
  fetchApi(`/api/escalations/${id}/resolve`, {
    method: 'POST',
    body: JSON.stringify({ resolution }),
  });
export const getHealthMetrics = (): Promise<AgentHealthMetric[]> => fetchApi('/api/metrics/health');
export const getCostAllocations = (): Promise<CostAllocation[]> => fetchApi('/api/metrics/costs');
