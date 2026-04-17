export default function LogsPage() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Log Search</h1>
      <div className="bg-white p-6 rounded shadow">
        <div className="flex gap-4 mb-4">
          <input type="text" placeholder="Search logs (e.g. error OR exception)" className="flex-1 border p-2 rounded" />
          <button className="bg-blue-600 text-white px-4 py-2 rounded">Search</button>
        </div>
        <div className="bg-gray-50 p-4 font-mono text-sm rounded overflow-x-auto">
          <div className="mb-2"><span className="text-gray-500">2026-04-17 12:00:01</span> <span className="text-blue-600">[INFO]</span> Application started</div>
          <div className="mb-2"><span className="text-gray-500">2026-04-17 12:05:22</span> <span className="text-yellow-600">[WARN]</span> High memory usage detected</div>
          <div className="mb-2"><span className="text-gray-500">2026-04-17 12:10:45</span> <span className="text-red-600">[ERROR]</span> Database connection timeout</div>
        </div>
      </div>
    </div>
  )
}
