import './globals.css';
import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import React from 'react';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'TaskQueue Dashboard',
  description: 'Manage and monitor distributed tasks',
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
          <header className="bg-white shadow-sm">
            <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
              <h1 className="text-xl font-bold text-gray-900">TaskQueue</h1>
              <nav className="flex space-x-4">
                <a href="/" className="text-gray-600 hover:text-gray-900">Dashboard</a>
                <a href="/jobs" className="text-gray-600 hover:text-gray-900">Jobs</a>
                <a href="/scheduled" className="text-gray-600 hover:text-gray-900">Scheduled</a>
              </nav>
            </div>
          </header>
          <main className="flex-1 max-w-7xl w-full mx-auto px-4 py-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
