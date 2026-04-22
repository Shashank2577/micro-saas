export default function Dashboard() {
  return (
    <div>
      <h2 className="text-2xl font-semibold mb-4">Dashboard</h2>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white shadow rounded-lg p-6">
          <h3 className="text-lg font-medium text-gray-900">Total Profiles</h3>
          <p className="mt-2 text-3xl font-bold text-blue-600">12</p>
        </div>
        <div className="bg-white shadow rounded-lg p-6">
          <h3 className="text-lg font-medium text-gray-900">Content Assessed</h3>
          <p className="mt-2 text-3xl font-bold text-green-600">45</p>
        </div>
        <div className="bg-white shadow rounded-lg p-6">
          <h3 className="text-lg font-medium text-gray-900">Avg Consistency Score</h3>
          <p className="mt-2 text-3xl font-bold text-purple-600">92%</p>
        </div>
      </div>
    </div>
  )
}
