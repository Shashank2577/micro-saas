export interface Donor {
  id: string;
  name: string;
  email: string | null;
  totalGiven: number;
  engagementScore: number;
  upgradePotential: string | null;
}

export interface Grant {
  id: string;
  title: string;
  funder: string;
  amount: number | null;
  deadline: string | null;
  status: string;
  draftContent: string | null;
}

export interface Impact {
  id: string;
  metricName: string;
  metricValue: number;
  narrative: string | null;
  dateRecorded: string;
}
