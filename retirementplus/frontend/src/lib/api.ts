import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8207',
  headers: {
    'Content-Type': 'application/json',
    'X-Tenant-ID': 'b0b691e8-78c6-4841-860b-ea2b7f329035', // default for dev
  },
});

export default api;
