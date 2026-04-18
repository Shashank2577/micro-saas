import { Budget, Category, Transaction, Alert, Target } from '../types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8209/api';

async function fetcher<T>(url: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE_URL}${url}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      'X-Tenant-ID': 'default-tenant',
      ...options?.headers,
    },
  });
  if (!res.ok) {
    throw new Error('API request failed');
  }
  return res.json();
}

export const api = {
  getBudgets: () => fetcher<Budget[]>('/budgets'),
  createBudget: (data: Partial<Budget>) => fetcher<Budget>('/budgets', { method: 'POST', body: JSON.stringify(data) }),
  getBudget: (id: string) => fetcher<Budget>(`/budgets/${id}`),
  getCategories: (budgetId: string) => fetcher<Category[]>(`/budgets/${budgetId}/categories`),
  createCategory: (budgetId: string, data: Partial<Category>) => fetcher<Category>(`/budgets/${budgetId}/categories`, { method: 'POST', body: JSON.stringify(data) }),
  getTransactions: (budgetId: string) => fetcher<Transaction[]>(`/transactions?budgetId=${budgetId}`),
  recordTransaction: (data: Partial<Transaction>) => fetcher<Transaction>('/transactions', { method: 'POST', body: JSON.stringify(data) }),
  categorizeTransaction: (data: { description: string; amount: number }) => fetcher<{ category: string }>('/transactions/categorize', { method: 'POST', body: JSON.stringify(data) }),
  getAlerts: () => fetcher<Alert[]>('/alerts'),
  createAlert: (data: Partial<Alert>) => fetcher<Alert>('/alerts', { method: 'POST', body: JSON.stringify(data) }),
  getOptimization: (budgetId: string) => fetcher<{ recommendations: string }>(`/optimization/recommendations/${budgetId}`),
  getTargets: () => fetcher<Target[]>('/targets'),
};
