import { Budget, BudgetItem, BudgetProposal } from "../types";

const API_URL = "http://localhost:8080/api/v1";

export async function getBudgets(): Promise<Budget[]> {
  const res = await fetch(`${API_URL}/budgets`);
  return res.json();
}

export async function createBudget(budget: Partial<Budget>): Promise<Budget> {
  const res = await fetch(`${API_URL}/budgets`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(budget),
  });
  return res.json();
}

export async function getBudgetItems(budgetId: string): Promise<BudgetItem[]> {
  const res = await fetch(`${API_URL}/budgets/${budgetId}/items`);
  return res.json();
}

export async function proposeBudget(request: any): Promise<BudgetProposal> {
  const res = await fetch(`${API_URL}/ai/propose-budget`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(request),
  });
  return res.json();
}
