import './globals.css';
import type { Metadata } from 'next';
import Link from 'next/link';

export const metadata: Metadata = {
  title: 'Data Lineage Tracker',
  description: 'Data Lineage and Governance Platform',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="bg-gray-50 min-h-screen">
        <nav className="bg-indigo-600 text-white shadow-lg">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between h-16">
              <div className="flex items-center">
                <Link href="/" className="flex-shrink-0 flex items-center font-bold text-xl">
                  DataLineageTracker
                </Link>
                <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
                  <Link href="/assets" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">
                    Assets
                  </Link>
                  <Link href="/lineage" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">
                    Lineage
                  </Link>
                  <Link href="/governance" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">
                    Governance
                  </Link>
                  <Link href="/audit" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-indigo-500">
                    Audit
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </nav>
        <main>{children}</main>
      </body>
    </html>
  );
}
