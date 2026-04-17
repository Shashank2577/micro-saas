import axios from 'axios';
import { logger } from './logger';

export class LitellmClient {
  private baseUrl: string;

  constructor(baseUrl: string = 'http://localhost:4000') {
    this.baseUrl = baseUrl;
  }

  async generateText(prompt: string, model: string = 'gpt-4o-mini', systemPrompt?: string): Promise<string> {
    try {
      const messages = [];
      if (systemPrompt) {
        messages.push({ role: 'system', content: systemPrompt });
      }
      messages.push({ role: 'user', content: prompt });

      const response = await axios.post(`${this.baseUrl}/v1/chat/completions`, {
        model,
        messages,
      }, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer fake-key` // Add dummy token if required by localhost mock
        }
      });

      return response.data.choices[0].message.content;
    } catch (error: any) {
      logger.error(`LiteLLM request failed: ${error.message}`);
      throw error;
    }
  }
}
