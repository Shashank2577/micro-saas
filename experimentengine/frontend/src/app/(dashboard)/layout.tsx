import React from 'react'
import Link from 'next/link'

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <div className="w-64 bg-white border-r border-gray-200">
        <div className="h-16 flex items-center px-6 border-b border-gray-200">
          <h1 className="text-lg font-semibold text-gray-900">ExperimentEngine</h1>
        </div>
        <nav className="p-4 space-y-1">
          <Link href="/experiments" className="block px-3 py-2 text-sm font-medium text-gray-900 bg-gray-100 rounded-md">Experiments</Link>
          <Link href="/insights" className="block px-3 py-2 text-sm font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50 rounded-md">Insights</Link>
          <Link href="/flags" className="block px-3 py-2 text-sm font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-50 rounded-md">Feature Flags</Link>
        </nav>
      </div>

      {/* Main content */}
      <div className="flex-1">
        <main className="p-8">
          {children}
        </main>
      </div>
    </div>
  )
}
