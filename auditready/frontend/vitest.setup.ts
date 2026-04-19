import '@testing-library/jest-dom'
import { vi } from 'vitest'
import React from 'react'

global.React = React;

vi.mock('next/navigation', () => ({
  useRouter() {
    return {
      push: vi.fn(),
      replace: vi.fn(),
      prefetch: vi.fn(),
    }
  },
  usePathname() {
    return '/'
  }
}))
