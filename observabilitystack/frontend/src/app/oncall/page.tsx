export default function OnCallPage() {
  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">On-Call Schedules</h1>
      <div className="bg-white p-6 rounded shadow mb-6">
        <h2 className="text-xl font-semibold mb-4">Current On-Call</h2>
        <div className="flex items-center gap-4 p-4 border rounded bg-blue-50 border-blue-200">
          <div className="w-12 h-12 bg-blue-600 text-white rounded-full flex items-center justify-center font-bold text-xl">
            JD
          </div>
          <div>
            <h3 className="font-bold text-lg">Jane Doe</h3>
            <p className="text-gray-600">Primary • Platform Engineering</p>
          </div>
        </div>
      </div>
    </div>
  )
}
