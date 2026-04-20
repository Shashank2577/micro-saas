export default function DashboardPage() {
  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="p-6 bg-white rounded-lg border shadow-sm">
          <h2 className="text-lg font-medium text-gray-600">Avg Resolution Time</h2>
          <p className="text-3xl font-bold mt-2">14.5m</p>
        </div>
        <div className="p-6 bg-white rounded-lg border shadow-sm">
          <h2 className="text-lg font-medium text-gray-600">Tickets Resolved</h2>
          <p className="text-3xl font-bold mt-2">45</p>
        </div>
        <div className="p-6 bg-white rounded-lg border shadow-sm">
          <h2 className="text-lg font-medium text-gray-600">CSAT Score</h2>
          <p className="text-3xl font-bold mt-2 text-green-600">4.8 / 5.0</p>
        </div>
      </div>
    </div>
  );
}
