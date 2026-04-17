import './globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Observability Stack',
  description: 'Monitoring and observability platform',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>
        <div className="flex h-screen bg-gray-100">
          <nav className="w-64 bg-slate-900 text-white p-4">
            <h1 className="text-xl font-bold mb-8">ObservabilityStack</h1>
            <ul className="space-y-2">
              <li><a href="/" className="block p-2 hover:bg-slate-800 rounded">Dashboard</a></li>
              <li><a href="/logs" className="block p-2 hover:bg-slate-800 rounded">Logs</a></li>
              <li><a href="/metrics" className="block p-2 hover:bg-slate-800 rounded">Metrics</a></li>
              <li><a href="/traces" className="block p-2 hover:bg-slate-800 rounded">Traces</a></li>
              <li><a href="/alerts" className="block p-2 hover:bg-slate-800 rounded">Alerts</a></li>
              <li><a href="/incidents" className="block p-2 hover:bg-slate-800 rounded">Incidents</a></li>
              <li><a href="/slos" className="block p-2 hover:bg-slate-800 rounded">SLOs</a></li>
              <li><a href="/oncall" className="block p-2 hover:bg-slate-800 rounded">On-Call</a></li>
            </ul>
          </nav>
          <main className="flex-1 overflow-auto p-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  )
}
