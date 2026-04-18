import '@testing-library/jest-dom/vitest'
import { vi } from 'vitest'

// Mock matchMedia
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(), // Deprecated
    removeListener: vi.fn(), // Deprecated
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
})

// Mock chart.js to prevent canvas errors in jsdom
vi.mock('react-chartjs-2', () => ({
  Line: () => null,
  Bar: () => null,
  Doughnut: () => null,
}))
