"use client";

import Link from 'next/link';

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-8">
      <h1 className="text-4xl font-bold mb-4">Restaurant Intel</h1>
      <p className="text-xl text-gray-600 mb-8">AI-powered restaurant operations platform</p>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl">
        <Link href="/menu" className="p-6 bg-white rounded-lg shadow hover:shadow-md transition">
          <h2 className="text-2xl font-semibold mb-2">Menu Analysis</h2>
          <p className="text-gray-600">Evaluate menu items using the Engineering Matrix.</p>
        </Link>

        <Link href="/ordering" className="p-6 bg-white rounded-lg shadow hover:shadow-md transition">
          <h2 className="text-2xl font-semibold mb-2">Predictive Ordering</h2>
          <p className="text-gray-600">AI-recommended orders to reduce food waste.</p>
        </Link>

        <Link href="/scheduling" className="p-6 bg-white rounded-lg shadow hover:shadow-md transition">
          <h2 className="text-2xl font-semibold mb-2">Staff Scheduling</h2>
          <p className="text-gray-600">Demand-based staffing predictions.</p>
        </Link>

        <Link href="/reviews" className="p-6 bg-white rounded-lg shadow hover:shadow-md transition">
          <h2 className="text-2xl font-semibold mb-2">Review Intelligence</h2>
          <p className="text-gray-600">Analyze reviews for operational insights.</p>
        </Link>
      </div>
    </div>
  );
}
