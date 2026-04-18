const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8114';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface WebhookEndpoint {
  id: string;
  name: string;
  url: string;
  secret: string;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
}

export interface WebhookEvent {
  id: string;
  source: string;
  eventType: string;
  payload: string;
  receivedAt: string;
}

export interface WebhookDelivery {
  id: string;
  event: WebhookEvent;
  endpoint: WebhookEndpoint;
  status: 'PENDING' | 'SUCCESS' | 'FAILED';
  statusCode: number | null;
  attemptCount: number;
  lastAttemptAt: string | null;
  nextAttemptAt: string | null;
  responseBody: string | null;
  createdAt: string;
}

export const api = {
  endpoints: {
    list: async (): Promise<WebhookEndpoint[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/endpoints`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch endpoints: ${res.status}`);
      return res.json();
    },
    create: async (data: { name: string; url: string }): Promise<WebhookEndpoint> => {
      const res = await fetch(`${BASE_URL}/api/v1/endpoints`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to create endpoint: ${res.status}`);
      return res.json();
    },
  },
  events: {
    list: async (): Promise<{ content: WebhookEvent[] }> => {
      const res = await fetch(`${BASE_URL}/api/v1/events?sort=receivedAt,desc`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch events: ${res.status}`);
      return res.json();
    },
  },
  deliveries: {
    list: async (): Promise<{ content: WebhookDelivery[] }> => {
      const res = await fetch(`${BASE_URL}/api/v1/deliveries?sort=createdAt,desc`, { headers });
      if (!res.ok) throw new Error(`Failed to fetch deliveries: ${res.status}`);
      return res.json();
    },
    replay: async (id: string): Promise<void> => {
      const res = await fetch(`${BASE_URL}/api/v1/deliveries/${id}/replay`, {
        method: 'POST', headers,
      });
      if (!res.ok) throw new Error(`Failed to replay delivery: ${res.status}`);
    },
  },
};
