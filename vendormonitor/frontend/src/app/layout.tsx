import './globals.css';
import type { Metadata } from 'next';
import { Inter } from 'next/font/google';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'VendorMonitor',
  description: 'AI-powered vendor performance tracking',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="min-h-screen bg-gray-50 flex flex-col">
          <header className="bg-white shadow-sm border-b">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
              <div className="flex items-center space-x-8">
                <a href="/" className="text-xl font-bold text-indigo-600">VendorMonitor</a>
                <nav className="hidden md:flex space-x-4">
                  <a href="/vendors" className="text-gray-600 hover:text-gray-900">Vendors</a>
                  <a href="/alerts" className="text-gray-600 hover:text-gray-900">Alerts</a>
                </nav>
              </div>
            </div>
          </header>
          <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
