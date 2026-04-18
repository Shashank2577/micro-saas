import './globals.css';
import type { Metadata } from 'next';
import { Inter } from 'next/font/google';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'Tenant Manager',
  description: 'AI-powered tenant operations platform',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="min-h-screen bg-gray-50 flex">
          <nav className="w-64 bg-white border-r border-gray-200 p-6 flex flex-col">
            <h1 className="text-xl font-bold text-gray-800 mb-8">Tenant Manager</h1>
            <div className="space-y-4">
              <a href="/" className="block text-gray-600 hover:text-blue-600">Dashboard</a>
              <a href="/tenants" className="block text-gray-600 hover:text-blue-600">Tenants</a>
            </div>
          </nav>
          <main className="flex-1 p-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
