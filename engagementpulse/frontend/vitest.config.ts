import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  plugins: [
    react({  }),
    {
      name: 'strip-use-client',
      enforce: 'pre',
      transform(code, id) {
        if (id.endsWith('.tsx') || id.endsWith('.ts')) {
          return code.replace(/['"]use client['"];?/, '');
        }
      }
    }
  ],
  test: {
    environment: 'jsdom',
    setupFiles: ['./vitest.setup.ts'],
    globals: true,
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
});
