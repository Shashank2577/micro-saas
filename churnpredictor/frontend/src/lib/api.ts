import axios from 'axios';

export const api = axios.create({
  baseURL: 'http://localhost:8146',
  headers: {
    'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
  }
});

// Mock interceptor for demo purposes if backend isn't available
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);
