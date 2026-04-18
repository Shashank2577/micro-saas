export interface Project {
  id: string;
  tenantId: string;
  name: string;
  description: string;
  targetAudience: string;
  createdAt: string;
  updatedAt: string;
}

export interface Interview {
  id: string;
  projectId: string;
  participantName: string;
  participantEmail: string;
  status: string;
  transcript: string;
  summary: string;
}

export interface Insight {
  id: string;
  projectId: string;
  theme: string;
  description: string;
  confidenceScore: number;
  supportingQuotes: string;
}

export interface Report {
  id: string;
  projectId: string;
  title: string;
  content: string;
  status: string;
}
