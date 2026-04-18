import Link from 'next/link';

export default function Home() {
  return (
    <main className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-8">
      <h1 className="text-4xl font-bold text-gray-900 mb-8">LogisticsAI</h1>
      <p className="text-xl text-gray-600 mb-8 max-w-2xl text-center">
        AI logistics intelligence platform. Carrier performance monitoring, demand forecasting, route optimization, and real-time exception handling.
      </p>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-4xl">
        <Link href="/dashboard" className="bg-white p-6 rounded-lg shadow-sm hover:shadow-md transition-shadow border border-gray-200">
          <h2 className="text-2xl font-semibold mb-2 text-indigo-600">Dashboard &rarr;</h2>
          <p className="text-gray-600">Overview of carrier performance and active routes.</p>
        </Link>
        <Link href="/routes" className="bg-white p-6 rounded-lg shadow-sm hover:shadow-md transition-shadow border border-gray-200">
          <h2 className="text-2xl font-semibold mb-2 text-indigo-600">Route Optimizer &rarr;</h2>
          <p className="text-gray-600">Manage routes and run AI-driven optimizations.</p>
        </Link>
        <Link href="/exceptions" className="bg-white p-6 rounded-lg shadow-sm hover:shadow-md transition-shadow border border-gray-200">
          <h2 className="text-2xl font-semibold mb-2 text-indigo-600">AI Agent &rarr;</h2>
          <p className="text-gray-600">Handle exceptions with AI recommendations.</p>
        </Link>
      </div>
    </main>
  );
}
