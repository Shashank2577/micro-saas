export const api = {
  get: async (url: string) => {
    const response = await fetch(`http://localhost:8147${url}`);
    if (!response.ok) throw new Error('Network response was not ok');
    return { data: await response.json() };
  },
  post: async (url: string, data: any) => {
    const response = await fetch(`http://localhost:8147${url}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!response.ok) throw new Error('Network response was not ok');
    return { data: await response.json() };
  }
};

export default api;
