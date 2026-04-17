export default function AlertsPage() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Alerts</h1>
      <div className="bg-white rounded shadow mb-6 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Alert</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Severity</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Triggered At</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            <tr>
              <td className="px-6 py-4 font-medium">High CPU Usage - Payment Service</td>
              <td className="px-6 py-4"><span className="px-2 py-1 text-xs font-semibold rounded bg-red-100 text-red-800">Critical</span></td>
              <td className="px-6 py-4"><span className="px-2 py-1 text-xs font-semibold rounded bg-yellow-100 text-yellow-800">Firing</span></td>
              <td className="px-6 py-4 text-sm text-gray-500">10 mins ago</td>
            </tr>
            <tr>
              <td className="px-6 py-4 font-medium">Memory Leak Detected - Auth Service</td>
              <td className="px-6 py-4"><span className="px-2 py-1 text-xs font-semibold rounded bg-orange-100 text-orange-800">Warning</span></td>
              <td className="px-6 py-4"><span className="px-2 py-1 text-xs font-semibold rounded bg-green-100 text-green-800">Resolved</span></td>
              <td className="px-6 py-4 text-sm text-gray-500">2 hours ago</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  )
}
