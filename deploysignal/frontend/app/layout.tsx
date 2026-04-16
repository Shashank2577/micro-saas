import './globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'DeploySignal Dashboard',
  description: 'Deployment Analytics and Risk Assessment',
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
