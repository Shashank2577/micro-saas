const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8091';

async function fetchAPI(endpoint: string, options: RequestInit = {}) {
  const url = `${API_URL}${endpoint}`;
  const response = await fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
  });

  if (!response.ok) {
    throw new Error(`API error: ${response.statusText}`);
  }

  return response.json();
}

export const api = {
  // ResearchQueryController
  getQueries: () => fetchAPI('/api/research/queries'),
  getQuery: (id: string) => fetchAPI(`/api/research/queries/${id}`),
  createQuery: (data: any) => fetchAPI('/api/research/queries', { method: 'POST', body: JSON.stringify(data) }),

  // MemoDraftController
  getMemos: () => fetchAPI('/api/memos'),
  getMemo: (id: string) => fetchAPI(`/api/memos/${id}`),
  createMemo: (data: any) => fetchAPI('/api/memos', { method: 'POST', body: JSON.stringify(data) }),
  updateMemo: (id: string, data: any) => fetchAPI(`/api/memos/${id}`, { method: 'PUT', body: JSON.stringify(data) }),

  // SourceCitationController
  getCitations: () => fetchAPI('/api/citations'),
  getCitation: (id: string) => fetchAPI(`/api/citations/${id}`),
  createCitation: (data: any) => fetchAPI('/api/citations', { method: 'POST', body: JSON.stringify(data) }),

  // ArgumentGraphController
  getArguments: () => fetchAPI('/api/arguments'),
  getArgumentGraph: (id: string) => fetchAPI(`/api/arguments/${id}/graph`),
  createArgument: (data: any) => fetchAPI('/api/arguments', { method: 'POST', body: JSON.stringify(data) }),

  // PrecedentNoteController
  getPrecedents: () => fetchAPI('/api/precedents'),
  getPrecedent: (id: string) => fetchAPI(`/api/precedents/${id}`),
  createPrecedent: (data: any) => fetchAPI('/api/precedents', { method: 'POST', body: JSON.stringify(data) }),

  // ReviewCommentController
  getComments: (memoId: string) => fetchAPI(`/api/memos/${memoId}/comments`),
  createComment: (memoId: string, data: any) => fetchAPI(`/api/memos/${memoId}/comments`, { method: 'POST', body: JSON.stringify(data) }),

  // WorkflowController
  getWorkflows: () => fetchAPI('/api/workflows'),
  createWorkflow: (data: any) => fetchAPI('/api/workflows', { method: 'POST', body: JSON.stringify(data) }),

  // ResearchAiController
  analyzeAi: (data: any) => fetchAPI('/api/ai/analyze', { method: 'POST', body: JSON.stringify(data) }),
  summarizeAi: (data: any) => fetchAPI('/api/ai/summarize', { method: 'POST', body: JSON.stringify(data) }),

  // HealthMetricsController
  getHealth: () => fetchAPI('/api/health'),
};
