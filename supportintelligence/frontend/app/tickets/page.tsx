import Link from 'next/link';

export default function TicketsPage() {
  const mockTickets = [
    { id: '1', externalId: 'ZD-100', category: 'Login Issue', status: 'OPEN', urgency: 'HIGH' },
    { id: '2', externalId: 'ZD-101', category: 'Billing', status: 'IN_PROGRESS', urgency: 'NORMAL' },
  ];

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Support Tickets</h1>
      <div className="bg-white rounded-lg border overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4 font-medium text-gray-600">Ticket ID</th>
              <th className="p-4 font-medium text-gray-600">Category</th>
              <th className="p-4 font-medium text-gray-600">Status</th>
              <th className="p-4 font-medium text-gray-600">Urgency</th>
              <th className="p-4 font-medium text-gray-600">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y">
            {mockTickets.map(t => (
              <tr key={t.id}>
                <td className="p-4">{t.externalId}</td>
                <td className="p-4">{t.category}</td>
                <td className="p-4">{t.status}</td>
                <td className="p-4">
                  <span className={`px-2 py-1 text-xs rounded-full ${t.urgency === 'HIGH' ? 'bg-red-100 text-red-800' : 'bg-gray-100'}`}>
                    {t.urgency}
                  </span>
                </td>
                <td className="p-4">
                  <Link href={`/tickets/${t.id}`} className="text-blue-600 hover:underline">
                    View
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
