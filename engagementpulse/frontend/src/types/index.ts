export interface Survey {
  id: string;
  title: string;
  description?: string;
  status: string;
  scheduledAt?: string;
  questions?: Question[];
}

export interface Question {
  id?: string;
  text: string;
  type: string;
  orderIndex: number;
}

export interface Alert {
  id: string;
  teamId: string;
  message: string;
  severity: string;
  resolved: boolean;
}
