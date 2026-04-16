import './globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'GhostWriter',
  description: 'AI long-form writing co-pilot trained on your personal voice',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  )
}
