import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Link from 'next/link';

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "VoiceAgentBuilder",
  description: "Voice AI Agent Platform",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <nav className="bg-gray-800 text-white p-4">
          <div className="max-w-7xl mx-auto flex gap-4">
            <Link href="/" className="font-bold hover:underline">Dashboard</Link>
            <Link href="/calls" className="hover:underline">Call Logs</Link>
          </div>
        </nav>
        {children}
      </body>
    </html>
  );
}
