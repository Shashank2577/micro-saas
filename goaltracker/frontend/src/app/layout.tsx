import type { Metadata } from 'next';
import './globals.css';

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
        <main className="min-h-screen bg-gray-50 text-gray-900">
          {children}
        </main>
      </body>
    </html>
  );
}
