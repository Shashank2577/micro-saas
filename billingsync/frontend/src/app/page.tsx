import Link from 'next/link';

export default function DashboardPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">BillingSync Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="p-6 bg-white shadow rounded-lg border border-gray-200">
          <h2 className="text-lg font-medium text-gray-500 mb-2">Total MRR</h2>
          <p className="text-3xl font-bold text-gray-900">$50,000</p>
        </div>
        <div className="p-6 bg-white shadow rounded-lg border border-gray-200">
          <h2 className="text-lg font-medium text-gray-500 mb-2">Active Subscriptions</h2>
          <p className="text-3xl font-bold text-gray-900">1,245</p>
        </div>
      </div>
      <div className="mt-8 flex gap-4">
        <Link href="/subscriptions" className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Manage Subscriptions</Link>
        <Link href="/plans" className="px-4 py-2 bg-white text-blue-600 border border-blue-600 rounded hover:bg-gray-50">View Plans</Link>
        <Link href="/invoices" className="px-4 py-2 bg-white text-gray-700 border border-gray-300 rounded hover:bg-gray-50">Invoices</Link>
        <Link href="/reports" className="px-4 py-2 bg-white text-gray-700 border border-gray-300 rounded hover:bg-gray-50">Reports</Link>
      </div>
    </div>
  );
}
