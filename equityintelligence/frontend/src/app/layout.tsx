import './globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Equity Intelligence',
  description: 'AI cap table + vesting calculation + funding round modeling',
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
