export interface Budget {
  id: string;
  name: string;
  fiscalYear: number;
  totalAmount: number;
  status: string;
}

export interface BudgetItem {
  id: string;
  category: string;
  department: string;
  allocatedAmount: number;
  actualAmount: number;
}

export interface Variance {
  id: string;
  varianceAmount: number;
  variancePercentage: number;
  explanation: string;
}

export interface BudgetProposal {
  proposalText: string;
}
