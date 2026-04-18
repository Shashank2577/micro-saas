import axios from 'axios';

export const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8118',
  headers: {
    'Content-Type': 'application/json',
    'X-Tenant-ID': 'test-tenant'
  }
});
