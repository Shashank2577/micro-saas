"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const litellm_1 = require("../src/shared/litellm");
const axios_1 = __importDefault(require("axios"));
jest.mock('axios');
const mockedAxios = axios_1.default;
describe('LitellmClient', () => {
    it('should generate text successfully', async () => {
        mockedAxios.post.mockResolvedValueOnce({
            data: { choices: [{ message: { content: 'Mocked output' } }] }
        });
        const client = new litellm_1.LitellmClient();
        const result = await client.generateText('Test prompt');
        expect(result).toBe('Mocked output');
        expect(mockedAxios.post).toHaveBeenCalledWith('http://localhost:4000/v1/chat/completions', expect.any(Object), expect.any(Object));
    });
});
