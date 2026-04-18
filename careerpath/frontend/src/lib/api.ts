const BASE_URL = 'http://localhost:8146/api/v1';
const TENANT_ID = 'tenant1'; // Default tenant for now

export const api = {
  async fetchWithTenant(endpoint: string, options: RequestInit = {}) {
    const res = await fetch(`${BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': TENANT_ID,
        ...options.headers,
      },
    });
    if (!res.ok) throw new Error(`API Error: ${res.statusText}`);
    return res.json();
  },
  
  getRoadmap: () => api.fetchWithTenant('/roadmaps'),
  getRoles: () => api.fetchWithTenant('/roles'),
  getSkills: (employeeId: string) => api.fetchWithTenant(`/employees/${employeeId}/skills`),
  getSkillGaps: (employeeId: string, roleId: string) => api.fetchWithTenant(`/employees/${employeeId}/skill-gaps?targetRoleId=${roleId}`),
  getRecommendations: (employeeId: string) => api.fetchWithTenant(`/employees/${employeeId}/recommend-roles`, { method: 'POST' }),
  getLearningPaths: (employeeId: string) => api.fetchWithTenant(`/employees/${employeeId}/learning-paths`, { method: 'POST' }),
};
