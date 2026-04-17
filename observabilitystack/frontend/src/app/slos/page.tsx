export default function SLOsPage() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Service Level Objectives (SLOs)</h1>
      <div className="bg-white p-6 rounded shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">Core API Availability</h2>
        <div className="flex justify-between items-end mb-2">
          <span className="text-gray-600">Target: 99.9%</span>
          <span className="text-2xl font-bold text-green-600">99.95%</span>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-4 mb-2">
          <div className="bg-green-500 h-4 rounded-full" style={{ width: '95%' }}></div>
        </div>
        <p className="text-sm text-gray-500 text-right">Error budget remaining: 50%</p>
      </div>
    </div>
  )
}
