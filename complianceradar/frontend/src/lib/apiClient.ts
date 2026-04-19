export const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8092/api/v1/regulations';
export const DEFAULT_TENANT_ID = '00000000-0000-0000-0000-000000000001';

export async function fetchWithTenant(endpoint: string, options: RequestInit = {}) {
    const headers = {
        'Content-Type': 'application/json',
        'X-Tenant-ID': DEFAULT_TENANT_ID,
        ...options.headers,
    };

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        throw new Error(`API error: ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}
