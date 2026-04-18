import Link from 'next/link';

export default function Dashboard() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">EcosystemMap Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="p-6 border rounded-lg shadow-sm">
          <h2 className="text-xl font-semibold mb-4">Ecosystem Map</h2>
          <p className="text-gray-600 mb-4">Visualize deployed apps and data flows.</p>
          <Link href="/map" className="text-blue-600 hover:underline">View Map &rarr;</Link>
        </div>
        <div className="p-6 border rounded-lg shadow-sm">
          <h2 className="text-xl font-semibold mb-4">Integration Opportunities</h2>
          <p className="text-gray-600 mb-4">AI-suggested integrations based on current ecosystem.</p>
          <Link href="/opportunities" className="text-blue-600 hover:underline">View Opportunities &rarr;</Link>
        </div>
      </div>
    </div>
  );
}
