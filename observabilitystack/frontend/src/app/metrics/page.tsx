export default function MetricsPage() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Metrics Explorer</h1>
      <div className="bg-white p-6 rounded shadow mb-6">
        <div className="flex gap-4 mb-4">
          <select className="border p-2 rounded flex-1">
            <option>cpu_usage_percent</option>
            <option>memory_usage_bytes</option>
            <option>http_requests_total</option>
          </select>
          <button className="bg-blue-600 text-white px-4 py-2 rounded">Query</button>
        </div>
        <div className="h-64 bg-gray-100 flex items-center justify-center rounded border border-dashed border-gray-300">
          <p className="text-gray-500">Chart Visualization (ECharts)</p>
        </div>
      </div>
    </div>
  )
}
