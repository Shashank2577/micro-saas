import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'API Manager',
  description: 'Ecosystem orchestrator for micro-SaaS portfolio',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
