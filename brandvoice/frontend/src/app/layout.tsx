import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'BrandVoice',
  description: 'AI-powered brand consistency platform',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="flex h-screen bg-gray-50">
          <div className="w-64 bg-white shadow-md p-4">
            <h1 className="text-xl font-bold mb-6">BrandVoice</h1>
            <nav className="space-y-2">
              <a href="/" className="block p-2 hover:bg-gray-100 rounded">Dashboard</a>
              <a href="/profiles" className="block p-2 hover:bg-gray-100 rounded">Brand Profiles</a>
              <a href="/guidelines" className="block p-2 hover:bg-gray-100 rounded">Guidelines</a>
              <a href="/content" className="block p-2 hover:bg-gray-100 rounded">Content Assets</a>
              <a href="/analysis" className="block p-2 hover:bg-gray-100 rounded">AI Analysis</a>
            </nav>
          </div>
          <main className="flex-1 p-8 overflow-auto">
            {children}
          </main>
        </div>
      </body>
    </html>
  )
}
