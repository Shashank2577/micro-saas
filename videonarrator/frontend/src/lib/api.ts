const BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8132';

const headers = {
  'Content-Type': 'application/json',
  'X-Tenant-ID': '00000000-0000-0000-0000-000000000001',
};

export interface VideoAsset {
  id: string;
  tenantId: string;
  title: string;
  fileUrl: string;
  durationSeconds: number;
  status: 'UPLOADED' | 'PROCESSING' | 'DONE' | 'FAILED';
  createdAt: string;
}

export interface RegisterVideoRequest {
  title: string;
  fileUrl: string;
  durationSeconds?: number;
}

export const api = {
  videos: {
    list: async (): Promise<VideoAsset[]> => {
      const res = await fetch(`${BASE_URL}/api/v1/videos`, { headers, cache: 'no-store' });
      if (!res.ok) throw new Error(`Failed to fetch videos: ${res.status}`);
      return res.json();
    },
    register: async (data: RegisterVideoRequest): Promise<VideoAsset> => {
      const res = await fetch(`${BASE_URL}/api/v1/videos`, {
        method: 'POST', headers, body: JSON.stringify(data),
      });
      if (!res.ok) throw new Error(`Failed to register video: ${res.status}`);
      return res.json();
    },
    transcribe: async (id: string) => {
        const res = await fetch(`${BASE_URL}/api/v1/videos/${id}/transcribe`, { method: 'POST', headers });
        if (!res.ok) throw new Error(`Failed to transcribe video: ${res.status}`);
        return res.json();
    },
    generateChapters: async (id: string) => {
        const res = await fetch(`${BASE_URL}/api/v1/videos/${id}/chapters`, { method: 'POST', headers });
        if (!res.ok) throw new Error(`Failed to generate chapters: ${res.status}`);
        return res.json();
    },
    deriveContent: async (id: string) => {
        const res = await fetch(`${BASE_URL}/api/v1/videos/${id}/derive`, { method: 'POST', headers });
        if (!res.ok) throw new Error(`Failed to derive content: ${res.status}`);
        return res.json();
    },
    getClips: async (id: string) => {
        const res = await fetch(`${BASE_URL}/api/v1/videos/${id}/clips`, { headers });
        if (!res.ok) throw new Error(`Failed to get clips: ${res.status}`);
        return res.json();
    }
  }
};
