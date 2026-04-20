export default function EscalationsPage() {
  const mockEscalations = [
    { id: '1', ticketId: 'ZD-100', signalType: 'Customer Anger', severity: 'CRITICAL', escalatedTo: 'Senior Support' },
  ];

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Escalation Signals</h1>
      <div className="bg-white rounded-lg border overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4 font-medium text-gray-600">Ticket ID</th>
              <th className="p-4 font-medium text-gray-600">Signal Type</th>
              <th className="p-4 font-medium text-gray-600">Severity</th>
              <th className="p-4 font-medium text-gray-600">Escalated To</th>
            </tr>
          </thead>
          <tbody className="divide-y">
            {mockEscalations.map(e => (
              <tr key={e.id}>
                <td className="p-4 text-blue-600 hover:underline cursor-pointer">{e.ticketId}</td>
                <td className="p-4">{e.signalType}</td>
                <td className="p-4">
                  <span className="bg-red-100 text-red-800 px-2 py-1 text-xs rounded-full">
                    {e.severity}
                  </span>
                </td>
                <td className="p-4">{e.escalatedTo}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
