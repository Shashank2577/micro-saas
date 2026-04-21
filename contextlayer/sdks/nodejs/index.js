const axios = require('axios');

class ContextClient {
  constructor({ apiKey, baseUrl = 'http://localhost:8136/api' }) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
    this.client = axios.create({
      baseURL: this.baseUrl,
      headers: { 'X-App-Id': this.apiKey }
    });
  }

  async getContext(customerId) {
    const res = await this.client.get(`/customers/${customerId}/context`);
    return res.data;
  }

  async updateContext(customerId, updates) {
    const res = await this.client.put(`/customers/${customerId}/context`, updates);
    return res.data;
  }
}

module.exports = { ContextClient };
