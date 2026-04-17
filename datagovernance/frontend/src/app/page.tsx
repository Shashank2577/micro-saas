import Link from 'next/link';

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-4xl font-bold text-gray-900 mb-8">Data Governance Dashboard</h1>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <Link href="/policies" className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Retention Policies</h2>
            <p className="text-gray-600">Manage data retention and lifecycle rules.</p>
          </Link>
          
          <Link href="/dsar" className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">DSAR Requests</h2>
            <p className="text-gray-600">Handle Data Subject Access and Deletion Requests.</p>
          </Link>

          <Link href="/consent" className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Consent Management</h2>
            <p className="text-gray-600">Track and manage user consent records.</p>
          </Link>

          <Link href="/lineage" className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Data Lineage</h2>
            <p className="text-gray-600">Trace data origins and transformations.</p>
          </Link>

          <Link href="/audit" className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Audit Logs</h2>
            <p className="text-gray-600">View compliance and data access audit trails.</p>
          </Link>

          <Link href="/pii" className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">PII Detection</h2>
            <p className="text-gray-600">AI-powered classification of sensitive data.</p>
          </Link>
        </div>
      </div>
    </div>
  );
}
