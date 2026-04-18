import '@testing-library/jest-dom/vitest';
import * as React from 'react';
import { expect, afterEach, vi } from 'vitest';
import { cleanup } from '@testing-library/react';

(globalThis as any).React = React;

afterEach(() => {
  cleanup();
});
