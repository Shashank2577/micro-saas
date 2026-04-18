import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8114',
  headers: {
    'Content-Type': 'application/json',
    'X-Tenant-ID': 'b0f8b7e2-4c2d-45b6-98ab-4318c64c76b9'
  }
});

export default api;
