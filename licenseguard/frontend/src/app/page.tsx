export default function Home() {
  return (
    <main className="min-h-screen p-8 bg-gray-50">
      <div className="max-w-4xl mx-auto space-y-8">
        <h1 className="text-3xl font-bold">LicenseGuard Dashboard</h1>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <a href="/repositories" className="p-6 bg-white rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Repositories</h2>
            <p className="text-gray-600">View and manage scanned repositories.</p>
          </a>

          <a href="/policies" className="p-6 bg-white rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Compliance Policies</h2>
            <p className="text-gray-600">Manage your license compliance rules.</p>
          </a>

          <a href="/scan-jobs" className="p-6 bg-white rounded-lg shadow hover:shadow-md transition">
            <h2 className="text-xl font-semibold mb-2">Scan Jobs</h2>
            <p className="text-gray-600">Monitor repository scanning progress.</p>
          </a>
        </div>
      </div>
    </main>
  );
}
