export default function InsightsPage() {
  const mockPatterns = [
    { id: '1', type: 'Login Issue', count: 15, firstSeen: '2 days ago' },
    { id: '2', type: 'Billing failure', count: 8, firstSeen: '5 days ago' },
  ];

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Product Insights & Patterns</h1>
      <div className="bg-white rounded-lg border overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4 font-medium text-gray-600">Pattern Type</th>
              <th className="p-4 font-medium text-gray-600">Occurrences</th>
              <th className="p-4 font-medium text-gray-600">First Seen</th>
              <th className="p-4 font-medium text-gray-600">Action</th>
            </tr>
          </thead>
          <tbody className="divide-y">
            {mockPatterns.map(p => (
              <tr key={p.id}>
                <td className="p-4">{p.type}</td>
                <td className="p-4">{p.count}</td>
                <td className="p-4">{p.firstSeen}</td>
                <td className="p-4">
                  <button className="text-blue-600 hover:underline">Link to Product Issue</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
