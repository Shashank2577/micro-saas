import './globals.css'
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'EngagementPulse',
  description: 'Employee engagement tracking platform',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <nav className="bg-white shadow-sm border-b p-4">
          <div className="max-w-7xl mx-auto flex items-center space-x-6">
            <h1 className="text-xl font-bold text-blue-600">EngagementPulse</h1>
            <a href="/" className="hover:text-blue-500">Dashboard</a>
            <a href="/surveys" className="hover:text-blue-500">Surveys</a>
            <a href="/alerts" className="hover:text-blue-500">Alerts</a>
          </div>
        </nav>
        <main className="max-w-7xl mx-auto bg-gray-50 min-h-screen">
          {children}
        </main>
      </body>
    </html>
  )
}
