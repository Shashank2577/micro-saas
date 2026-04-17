import './globals.css'
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import Link from 'next/link'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'AgentOps',
  description: 'AI agent observability and governance platform',
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
          <aside className="w-64 bg-white shadow-md">
            <div className="p-4 border-b">
              <h1 className="text-xl font-bold">AgentOps</h1>
            </div>
            <nav className="p-4 space-y-2">
              <Link href="/" className="block p-2 hover:bg-gray-50 rounded">Dashboard</Link>
              <Link href="/runs" className="block p-2 hover:bg-gray-50 rounded">Runs</Link>
              <Link href="/escalations" className="block p-2 hover:bg-gray-50 rounded">Escalations</Link>
              <Link href="/costs" className="block p-2 hover:bg-gray-50 rounded">Costs</Link>
            </nav>
          </aside>
          <main className="flex-1 overflow-y-auto p-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  )
}
