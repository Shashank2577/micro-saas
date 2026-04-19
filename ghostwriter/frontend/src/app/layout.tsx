import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "GhostWriter",
  description: "AI Content Generation",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <div className="flex h-screen bg-gray-100">
          <aside className="w-64 bg-white shadow-md p-4 flex flex-col">
            <h1 className="text-2xl font-bold mb-8 text-indigo-600">GhostWriter</h1>
            <nav className="flex-1 space-y-2">
              <a href="/" className="block p-2 rounded hover:bg-gray-100 font-medium">Dashboard</a>
              <a href="/documents" className="block p-2 rounded hover:bg-gray-100 font-medium">Documents</a>
              <a href="/generate" className="block p-2 rounded bg-indigo-50 text-indigo-700 font-medium mt-4">Generate New</a>
            </nav>
          </aside>
          <main className="flex-1 overflow-auto p-8">
            {children}
          </main>
        </div>
      </body>
    </html>
  );
}
