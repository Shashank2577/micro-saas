import React from 'react';
import './globals.css';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className="bg-gray-100 min-h-screen">
        <div className="flex h-screen bg-gray-100">
          <aside className="w-64 bg-white shadow-md">
            <div className="p-4">
              <h1 className="text-xl font-bold text-gray-800">LegalResearch</h1>
            </div>
            <nav className="mt-4">
              <a href="/" className="block py-2 px-4 text-gray-700 hover:bg-gray-200">Dashboard</a>
              <a href="/queries" className="block py-2 px-4 text-gray-700 hover:bg-gray-200">Queries</a>
              <a href="/memos" className="block py-2 px-4 text-gray-700 hover:bg-gray-200">Memos</a>
              <a href="/citations" className="block py-2 px-4 text-gray-700 hover:bg-gray-200">Citations</a>
              <a href="/precedents" className="block py-2 px-4 text-gray-700 hover:bg-gray-200">Precedents</a>
            </nav>
          </aside>
          <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100 p-6">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
