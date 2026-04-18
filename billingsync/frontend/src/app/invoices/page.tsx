'use client';

export default function InvoicesPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Invoices</h1>
      <table className="min-w-full bg-white border border-gray-200">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">ID</th>
            <th className="py-2 px-4 border-b">Amount Due</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Due Date</th>
            <th className="py-2 px-4 border-b">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td className="py-2 px-4 border-b text-center">INV-001</td>
            <td className="py-2 px-4 border-b text-center">$150.00</td>
            <td className="py-2 px-4 border-b text-center"><span className="px-2 py-1 bg-green-100 text-green-800 rounded">PAID</span></td>
            <td className="py-2 px-4 border-b text-center">2023-11-01</td>
            <td className="py-2 px-4 border-b text-center"><button className="text-blue-600 hover:underline">View PDF</button></td>
          </tr>
        </tbody>
      </table>
    </div>
  );
}
