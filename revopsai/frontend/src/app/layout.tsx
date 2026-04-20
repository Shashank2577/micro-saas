import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "RevOpsAI",
  description: "AI revenue operations platform",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <nav className="p-4 bg-gray-800 text-white flex gap-4">
          <a href="/">Dashboard</a>
          <a href="/pipeline">Pipeline</a>
          <a href="/cac-ltv">CAC/LTV</a>
          <a href="/forecast">Forecast</a>
          <a href="/gtm-gaps">GTM Gaps</a>
          <a href="/nlp">NLP Query</a>
        </nav>
        <main className="p-8">{children}</main>
      </body>
    </html>
  );
}
