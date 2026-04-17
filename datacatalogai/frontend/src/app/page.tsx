import Link from 'next/link';

export default function Home() {
  return (
    <div className="p-8 max-w-6xl mx-auto">
      <header className="mb-12 flex justify-between items-center">
        <div>
          <h1 className="text-4xl font-bold tracking-tight text-gray-900">DataCatalogAI</h1>
          <p className="mt-2 text-lg text-gray-600">Self-maintaining data catalog with auto-docs, PII detection, and lineage.</p>
        </div>
        <nav className="flex space-x-4">
            <Link href="/sources" className="text-blue-600 hover:text-blue-800">Data Sources</Link>
            <Link href="/glossary" className="text-blue-600 hover:text-blue-800">Glossary</Link>
        </nav>
      </header>

      <div className="bg-white rounded-lg shadow-sm border p-6 mb-8">
        <h2 className="text-2xl font-semibold mb-4">Semantic Search</h2>
        <form className="flex gap-4">
          <input
            type="text"
            placeholder="Search assets (e.g. 'monthly revenue')..."
            className="flex-1 rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-3 border"
          />
          <button type="submit" className="bg-blue-600 text-white px-6 py-3 rounded-md hover:bg-blue-700">Search</button>
        </form>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <h3 className="text-xl font-medium mb-2">Recently Documented</h3>
          <ul className="divide-y text-sm">
            <li className="py-3 flex justify-between"><span>public.orders</span> <span className="text-gray-500 text-xs">2 mins ago</span></li>
            <li className="py-3 flex justify-between"><span>public.users</span> <span className="text-gray-500 text-xs">1 hr ago</span></li>
            <li className="py-3 flex justify-between"><span>public.products</span> <span className="text-gray-500 text-xs">3 hrs ago</span></li>
          </ul>
        </div>
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <h3 className="text-xl font-medium mb-2">PII Alerts</h3>
          <ul className="divide-y text-sm text-red-600">
            <li className="py-3 flex justify-between"><span>customer_email in orders</span> <span className="font-bold">EMAIL (0.98)</span></li>
            <li className="py-3 flex justify-between"><span>ssn in users</span> <span className="font-bold">SSN (0.95)</span></li>
          </ul>
        </div>
      </div>
    </div>
  );
}
