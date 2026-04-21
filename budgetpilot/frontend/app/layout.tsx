import './globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'BudgetPilot',
  description: 'AI-Native Budget Management',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className="bg-gray-100 min-h-screen text-gray-900">
        <nav className="bg-white shadow p-4 mb-8">
          <div className="container mx-auto flex gap-4">
            <a href="/dashboard" className="font-bold text-xl">BudgetPilot</a>
            <a href="/budgets" className="text-gray-600 hover:text-gray-900 ml-4 mt-1">Budgets</a>
            <a href="/planning" className="text-gray-600 hover:text-gray-900 ml-4 mt-1">AI Planning</a>
          </div>
        </nav>
        <main className="container mx-auto p-4">
          {children}
        </main>
      </body>
    </html>
  )
}
