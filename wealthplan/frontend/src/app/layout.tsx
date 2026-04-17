import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "WealthPlan",
  description: "AI-powered financial planning and goal tracking platform.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="antialiased">
        {children}
      </body>
    </html>
  );
}
