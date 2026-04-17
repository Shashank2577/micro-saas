export default function TracesPage() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Distributed Tracing</h1>
      <div className="bg-white p-6 rounded shadow">
        <h2 className="text-xl font-semibold mb-4">Recent Traces</h2>
        <ul className="space-y-4">
          <li className="border p-4 rounded hover:bg-gray-50 cursor-pointer">
            <div className="flex justify-between mb-2">
              <span className="font-medium text-blue-600">GET /api/users/profile</span>
              <span className="text-gray-500">120ms</span>
            </div>
            <div className="text-sm text-gray-600">4 spans • 2 services (api-gateway, user-service)</div>
          </li>
          <li className="border p-4 rounded hover:bg-gray-50 cursor-pointer border-l-4 border-l-red-500">
            <div className="flex justify-between mb-2">
              <span className="font-medium text-blue-600">POST /api/payments</span>
              <span className="text-gray-500">1540ms</span>
            </div>
            <div className="text-sm text-gray-600">12 spans • 4 services (api-gateway, payment-service, fraud-check, email-service)</div>
          </li>
        </ul>
      </div>
    </div>
  )
}
