export class ContextClient {
  constructor(config: { apiKey: string, baseUrl?: string });
  getContext(customerId: string): Promise<any>;
  updateContext(customerId: string, updates: any): Promise<any>;
}
