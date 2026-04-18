import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { Providers } from "./providers";
import Link from 'next/link';

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "BudgetMaster",
  description: "Personal budgeting and expense control platform",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <Providers>
          <div className="flex min-h-screen bg-gray-50">
            <nav className="w-64 bg-white border-r p-4 space-y-4 shadow-sm hidden md:block">
              <h2 className="text-xl font-bold mb-6 text-blue-600">BudgetMaster</h2>
              <div className="space-y-2">
                <Link href="/" className="block p-2 rounded hover:bg-gray-100">Dashboard</Link>
                <Link href="/budgets" className="block p-2 rounded hover:bg-gray-100">Setup Budget</Link>
                <Link href="/categories" className="block p-2 rounded hover:bg-gray-100">Categories</Link>
                <Link href="/transactions" className="block p-2 rounded hover:bg-gray-100">Transactions</Link>
                <Link href="/targets" className="block p-2 rounded hover:bg-gray-100">Targets</Link>
                <Link href="/review" className="block p-2 rounded hover:bg-gray-100">Review Checklist</Link>
                <Link href="/family" className="block p-2 rounded hover:bg-gray-100">Family</Link>
                <Link href="/irregular" className="block p-2 rounded hover:bg-gray-100">Irregular Expenses</Link>
                <Link href="/alerts" className="block p-2 rounded hover:bg-gray-100">Alerts</Link>
              </div>
            </nav>
            <div className="flex-1 overflow-auto">
              {children}
            </div>
          </div>
        </Providers>
      </body>
    </html>
  );
}
