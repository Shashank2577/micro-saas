import '@testing-library/jest-dom/vitest'
import { vi } from 'vitest'

// Mock generic Next.js navigation and API functions
vi.mock('next/navigation', () => ({
  useRouter: () => ({ push: vi.fn() })
}))
