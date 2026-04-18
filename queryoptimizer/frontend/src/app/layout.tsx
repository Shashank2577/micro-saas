import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Link from "next/link";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Query Optimizer",
  description: "Intelligent query optimization and performance tuning platform",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="flex h-screen bg-gray-50">
          {/* Sidebar */}
          <aside className="w-64 bg-white border-r flex flex-col">
            <div className="p-6 border-b">
              <h1 className="text-2xl font-bold text-blue-600">Query Optimizer</h1>
            </div>
            <nav className="flex-1 p-4 flex flex-col gap-2">
              <Link href="/" className="px-4 py-2 rounded hover:bg-gray-100 text-gray-700">Dashboard</Link>
              <Link href="/fingerprints" className="px-4 py-2 rounded hover:bg-gray-100 text-gray-700">Query Fingerprints</Link>
              <Link href="/recommendations" className="px-4 py-2 rounded hover:bg-gray-100 text-gray-700">Recommendations</Link>
              <Link href="/indexes" className="px-4 py-2 rounded hover:bg-gray-100 text-gray-700">Index Advisor</Link>
              <Link href="/upload" className="px-4 py-2 rounded hover:bg-gray-100 text-gray-700">Upload Logs</Link>
            </nav>
          </aside>

          {/* Main content */}
          <main className="flex-1 overflow-auto p-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
