export default function IncidentsPage() {
  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Incident Management</h1>
        <button className="bg-red-600 text-white px-4 py-2 rounded font-medium">Declare Incident</button>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded shadow border-t-4 border-red-500">
          <div className="flex justify-between items-start mb-4">
            <h2 className="text-xl font-bold">INC-104: Payment Gateway Outage</h2>
            <span className="bg-red-100 text-red-800 text-xs px-2 py-1 rounded font-bold">SEV-1</span>
          </div>
          <p className="text-gray-600 mb-4">Users are unable to process payments. 500 errors spiking on /checkout endpoint.</p>
          <div className="text-sm text-gray-500 flex justify-between">
            <span>Status: <strong>Investigating</strong></span>
            <span>Started: 15 mins ago</span>
          </div>
        </div>
        <div className="bg-white p-6 rounded shadow border-t-4 border-yellow-500">
          <div className="flex justify-between items-start mb-4">
            <h2 className="text-xl font-bold">INC-103: Delayed Email Notifications</h2>
            <span className="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded font-bold">SEV-3</span>
          </div>
          <p className="text-gray-600 mb-4">Email queue is backing up, causing delivery delays of up to 10 minutes.</p>
          <div className="text-sm text-gray-500 flex justify-between">
            <span>Status: <strong>Monitoring</strong></span>
            <span>Started: 2 hours ago</span>
          </div>
        </div>
      </div>
    </div>
  )
}
