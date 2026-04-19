export interface Model {
  id: string;
  name: string;
  purpose: string;
  riskTier: string;
  status: string;
  version: string;
}

export interface FairnessMetric {
  id: string;
  metric: string;
  protectedAttr: string;
  groupA: string;
  groupB: string;
  value: number;
  threshold: number;
  passed: boolean;
}

export interface DriftSignal {
  id: string;
  kind: string;
  feature: string;
  score: number;
  passed: boolean;
}

export interface Violation {
  id: string;
  type: string;
  severity: string;
  detail: string;
  createdAt: string;
}
