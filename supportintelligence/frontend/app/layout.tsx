import './globals.css';
import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'SupportIntelligence',
  description: 'AI Support Co-Pilot',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="min-h-screen bg-gray-50 text-gray-900">
        <nav className="bg-white border-b border-gray-200 px-4 py-3">
          <div className="flex space-x-4">
            <a href="/dashboard" className="font-medium text-gray-900 hover:text-blue-600">Dashboard</a>
            <a href="/tickets" className="font-medium text-gray-900 hover:text-blue-600">Tickets</a>
            <a href="/escalations" className="font-medium text-gray-900 hover:text-blue-600">Escalations</a>
            <a href="/insights" className="font-medium text-gray-900 hover:text-blue-600">Insights</a>
          </div>
        </nav>
        <main className="p-4">
          {children}
        </main>
      </body>
    </html>
  );
}
