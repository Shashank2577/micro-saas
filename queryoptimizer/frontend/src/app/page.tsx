import Link from 'next/link';

export default function Home() {
  return (
    <div className="flex flex-col gap-6">
      <h1 className="text-3xl font-bold">Dashboard</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow border">
          <h2 className="text-xl font-semibold mb-2">Total Queries</h2>
          <p className="text-4xl font-bold text-blue-600">1,245</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow border">
          <h2 className="text-xl font-semibold mb-2">Optimizations Found</h2>
          <p className="text-4xl font-bold text-green-600">42</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow border">
          <h2 className="text-xl font-semibold mb-2">Active Alerts</h2>
          <p className="text-4xl font-bold text-red-600">3</p>
        </div>
      </div>
      
      <div className="mt-8 flex gap-4">
        <Link href="/upload" className="bg-blue-600 text-white px-4 py-2 rounded shadow hover:bg-blue-700">
          Upload Slow Log
        </Link>
        <Link href="/fingerprints" className="bg-gray-100 text-gray-800 px-4 py-2 rounded shadow hover:bg-gray-200 border">
          View Fingerprints
        </Link>
      </div>
    </div>
  );
}
