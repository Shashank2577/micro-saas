import { api } from '@/lib/api';
import Link from 'next/link';

export default async function DashboardPage() {
  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">APIEvolver</h1>
          <p className="text-gray-500 mt-1">API Contract Management and Backward Compatibility Checking</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <Link href="/api-specs" className="block bg-white p-6 rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">API Specs</h2>
            <p className="text-gray-600">Manage OpenAPI specifications.</p>
          </Link>
          <Link href="/api-versions" className="block bg-white p-6 rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">API Versions</h2>
            <p className="text-gray-600">Track API versions and lifecycles.</p>
          </Link>
          <Link href="/breaking-changes" className="block bg-white p-6 rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Breaking Changes</h2>
            <p className="text-gray-600">Review detected breaking changes.</p>
          </Link>
          <Link href="/compatibility-reports" className="block bg-white p-6 rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Compatibility Reports</h2>
            <p className="text-gray-600">View generated compatibility reports.</p>
          </Link>
          <Link href="/deprecation-notices" className="block bg-white p-6 rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Deprecation Notices</h2>
            <p className="text-gray-600">Manage endpoint deprecations.</p>
          </Link>
          <Link href="/sdk-artifacts" className="block bg-white p-6 rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">SDK Artifacts</h2>
            <p className="text-gray-600">Manage generated SDKs.</p>
          </Link>
        </div>
      </div>
    </main>
  );
}
