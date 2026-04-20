import axios from 'axios';

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'X-Tenant-ID': 'test-tenant-123'
  }
});

export default api;
