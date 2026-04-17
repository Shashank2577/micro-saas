import { Suspense } from 'react';
import Link from 'next/link';

export default function Dashboard() {
  return (
    <div className="space-y-6">
      <header className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold tracking-tight text-gray-900">CallIntelligence Dashboard</h1>
          <p className="mt-2 text-sm text-gray-600">Overview of recent calls and team metrics</p>
        </div>
        <div className="flex gap-4">
            <Link href="/calls/new" className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700">
              Upload Call
            </Link>
        </div>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow border border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Total Calls Analyzed</h2>
            <p className="mt-2 text-3xl font-bold text-indigo-600">124</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow border border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Avg Talk Ratio (Rep)</h2>
            <p className="mt-2 text-3xl font-bold text-indigo-600">48%</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow border border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Action Items Extracted</h2>
            <p className="mt-2 text-3xl font-bold text-indigo-600">312</p>
        </div>
      </div>
      
      <div className="bg-white rounded-lg shadow border border-gray-200 p-6">
         <h2 className="text-xl font-semibold mb-4">Quick Links</h2>
         <ul className="space-y-2">
            <li><Link href="/calls" className="text-indigo-600 hover:underline">View All Calls</Link></li>
            <li><Link href="/scorecards" className="text-indigo-600 hover:underline">Rep Scorecards</Link></li>
         </ul>
      </div>
    </div>
  );
}
