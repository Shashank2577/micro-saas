export interface Event {
  id: string;
  tenantId: string;
  userId: string;
  eventName: string;
  properties: Record<string, any>;
  createdAt: string;
}

export interface Metric {
  id: string;
  tenantId: string;
  metricName: string;
  value: number;
  dimensions: Record<string, string>;
  createdAt: string;
}

export interface AiInsight {
  id: string;
  tenantId: string;
  title: string;
  description: string;
  recommendation: string;
  dataReferences: Record<string, any>;
  createdAt: string;
}

export interface Cohort {
  id: string;
  tenantId: string;
  name: string;
  description: string;
  criteria: Record<string, any>;
  createdAt: string;
  updatedAt: string;
}
