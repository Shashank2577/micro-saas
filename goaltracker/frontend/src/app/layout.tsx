import type { Metadata } from 'next';
import './globals.css';
import QueryProvider from './QueryProvider';

export const metadata: Metadata = {
  title: 'GoalTracker',
  description: 'Financial goal setting and tracking',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <QueryProvider>
          <main className="min-h-screen bg-gray-50 text-gray-900">
            {children}
          </main>
        </QueryProvider>
      </body>
    </html>
  );
}
