const api = {
  get: async (url: string, config?: any) => {
    const search = config?.params?.search || '';
    const res = await fetch(`http://localhost:8113${url}?search=${search}`, {
        headers: {
            'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
        }
    });
    const data = await res.json();
    return { data };
  },
  post: async (url: string, data: any, config?: any) => {
      const res = await fetch(`http://localhost:8113${url}`, {
        method: 'POST',
        headers: {
            'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
        },
        body: data
      });
      const responseData = await res.json();
      return { data: responseData };
  },
  delete: async (url: string, config?: any) => {
      const res = await fetch(`http://localhost:8113${url}`, {
        method: 'DELETE',
        headers: {
            'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
        }
      });
      return { data: null };
  }
};
export default api;
