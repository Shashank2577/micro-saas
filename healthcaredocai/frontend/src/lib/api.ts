const API_BASE = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8150/api';
const TENANT_ID = process.env.NEXT_PUBLIC_TENANT_ID || '00000000-0000-0000-0000-000000000001';

export const api = {
  transcribe: async (patientId: string, clinicianId: string, audioBase64?: string) => {
    const res = await fetch(`${API_BASE}/encounters/transcribe`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': TENANT_ID,
      },
      body: JSON.stringify({ patientId, clinicianId, audioBase64 }),
    });
    if (!res.ok) throw new Error('Transcription failed');
    return res.json();
  },
  generateNote: async (encounterId: string, noteType: string, specialty: string) => {
    const res = await fetch(`${API_BASE}/notes/generate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': TENANT_ID,
      },
      body: JSON.stringify({ encounterId, noteType, specialty }),
    });
    if (!res.ok) throw new Error('Note generation failed');
    return res.json();
  },
  syncToEHR: async (noteId: string, ehrSystem: string) => {
    const url = ehrSystem.toLowerCase() === 'epic' ? `${API_BASE}/ehr/epic/sync` : `${API_BASE}/ehr/sync`;
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': TENANT_ID,
      },
      body: JSON.stringify({ noteId, ehrSystem }),
    });
    if (!res.ok) throw new Error('EHR sync failed');
    return res.text();
  }
};
