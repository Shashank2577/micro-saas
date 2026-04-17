export default function Dashboard() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Overview Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500 text-sm font-medium">Active Incidents</h3>
          <p className="text-3xl font-bold text-red-600">2</p>
        </div>
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500 text-sm font-medium">Alerts (24h)</h3>
          <p className="text-3xl font-bold text-yellow-600">14</p>
        </div>
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500 text-sm font-medium">System Health</h3>
          <p className="text-3xl font-bold text-green-600">98.5%</p>
        </div>
      </div>
      <div className="bg-white p-6 rounded shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">Health Checks</h2>
        <table className="min-w-full divide-y divide-gray-200">
          <thead>
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Service</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            <tr>
              <td className="px-6 py-4">Auth Service</td>
              <td className="px-6 py-4"><span className="px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">Healthy</span></td>
            </tr>
            <tr>
              <td className="px-6 py-4">Payment Gateway</td>
              <td className="px-6 py-4"><span className="px-2 py-1 text-xs font-semibold rounded-full bg-yellow-100 text-yellow-800">Degraded</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  )
}
