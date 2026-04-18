import './globals.css';
import type { Metadata } from 'next';
import Link from 'next/link';

export const metadata: Metadata = {
  title: 'RetailIntelligence',
  description: 'AI Retail Operations Platform',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <nav className="p-4 bg-blue-600 text-white flex gap-4">
          <Link href="/">Dashboard</Link>
          <Link href="/skus">SKUs</Link>
          <Link href="/pricing">Pricing Actions</Link>
        </nav>
        <main className="p-4">
          {children}
        </main>
      </body>
    </html>
  );
}
