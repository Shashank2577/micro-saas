import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8200/api',
  headers: {
    'X-Tenant-ID': '123e4567-e89b-12d3-a456-426614174000', // Mock Tenant ID for dev
  },
});

export default api;
