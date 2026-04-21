import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'SocialIntelligence',
  description: 'Multi-platform social media analytics',
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
            <h1 className="text-xl font-bold mb-6">SocialIntelligence</h1>
            <nav className="space-y-2">
              <a href="/" className="block p-2 hover:bg-gray-100 rounded">Dashboard</a>
              <a href="/connect" className="block p-2 hover:bg-gray-100 rounded">Connect Platforms</a>
              <a href="/content" className="block p-2 hover:bg-gray-100 rounded">Top Content</a>
              <a href="/audience" className="block p-2 hover:bg-gray-100 rounded">Audience</a>
              <a href="/recommendations" className="block p-2 hover:bg-gray-100 rounded">Recommendations</a>
              <a href="/growth" className="block p-2 hover:bg-gray-100 rounded">Growth Projection</a>
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
