export interface Budget {
  id: string;
  name: string;
  monthlyIncome: number;
  type: string;
  rollingMode: boolean;
  month: number;
  year: number;
}

export interface Category {
  id: string;
  budgetId: string;
  name: string;
  allocatedAmount: number;
  type: string;
  carryover: boolean;
}

export interface Transaction {
  id: string;
  budgetId: string;
  categoryId: string | null;
  amount: number;
  date: string;
  description: string;
  status: string;
}

export interface Alert {
  id: string;
  categoryId: string;
  thresholdPercent: number;
  thresholdAmount: number;
  triggered: boolean;
}

export interface Target {
  id: string;
  name: string;
  targetAmount: number;
  currentAmount: number;
  deadline: string;
}
