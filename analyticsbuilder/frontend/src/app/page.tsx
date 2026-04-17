"use client";

import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen p-4 bg-gray-50 text-black">
      <h1 className="text-4xl font-bold mb-4">Welcome to AnalyticsBuilder</h1>
      <p className="text-lg text-gray-700 mb-8 max-w-2xl text-center">
        Create sophisticated dashboards without coding, featuring real-time data, interactive filters, drill-down capabilities, and sharing.
      </p>

      <Link href="/dashboards" className="bg-blue-600 text-white px-6 py-3 rounded-lg shadow hover:bg-blue-700 font-semibold text-lg">
        View Dashboards
      </Link>
    </div>
  );
}
