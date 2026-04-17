import './globals.css'
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'

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
        <div className="flex h-screen bg-gray-100">
          <nav className="w-64 bg-white border-r">
            <div className="p-4 text-xl font-bold border-b">SocialIntelligence</div>
            <ul className="p-4 space-y-2">
              <li><a href="/dashboard" className="block p-2 hover:bg-gray-100 rounded">Dashboard</a></li>
              <li><a href="/platforms" className="block p-2 hover:bg-gray-100 rounded">Platforms</a></li>
              <li><a href="/audience" className="block p-2 hover:bg-gray-100 rounded">Audience</a></li>
              <li><a href="/content" className="block p-2 hover:bg-gray-100 rounded">Content</a></li>
              <li><a href="/growth" className="block p-2 hover:bg-gray-100 rounded">Growth AI</a></li>
            </ul>
          </nav>
          <main className="flex-1 overflow-y-auto p-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  )
}
