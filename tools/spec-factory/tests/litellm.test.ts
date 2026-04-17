import { LitellmClient } from '../src/shared/litellm';
import axios from 'axios';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('LitellmClient', () => {
  it('should generate text successfully', async () => {
    mockedAxios.post.mockResolvedValueOnce({
      data: { choices: [{ message: { content: 'Mocked output' } }] }
    });

    const client = new LitellmClient();
    const result = await client.generateText('Test prompt');
    
    expect(result).toBe('Mocked output');
    expect(mockedAxios.post).toHaveBeenCalledWith(
      'http://localhost:4000/v1/chat/completions',
      expect.any(Object),
      expect.any(Object)
    );
  });
});
