import axios from 'axios';

const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api',
    headers: {
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
    }
});

export default api;
