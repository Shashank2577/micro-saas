import Link from 'next/link';

export default async function DashboardPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">TelemetryCore</h1>
        <p className="mt-1 text-sm text-gray-500">
          Centralized analytics and telemetry service.
        </p>
      </div>

      <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <Link href="/dashboard" className="block p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100">
            <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">Dashboard</h5>
            <p className="font-normal text-gray-700">View real-time active users.</p>
        </Link>
        <Link href="/metrics" className="block p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100">
            <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">Metrics</h5>
            <p className="font-normal text-gray-700">Define custom metrics.</p>
        </Link>
        <Link href="/cohorts" className="block p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100">
            <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">Cohorts</h5>
            <p className="font-normal text-gray-700">View cohort analysis.</p>
        </Link>
        <Link href="/funnels" className="block p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100">
            <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">Funnels</h5>
            <p className="font-normal text-gray-700">View funnel conversions.</p>
        </Link>
        <Link href="/experiments" className="block p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100">
            <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">A/B Tests</h5>
            <p className="font-normal text-gray-700">View experiment results.</p>
        </Link>
      </div>
    </div>
  );
}
