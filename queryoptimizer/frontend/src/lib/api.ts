import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8166',
  headers: {
    'X-Tenant-ID': '00000000-0000-0000-0000-000000000000', // Default local tenant
  },
});

export default api;
