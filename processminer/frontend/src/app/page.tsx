"use client";

import Link from 'next/link';

export default function Home() {
  return (
    <div className="flex flex-col min-h-screen">
      <header className="bg-gray-800 text-white p-4">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold">ProcessMiner</h1>
          <nav className="space-x-4">
            <Link href="/ingestion" className="hover:underline">Ingestion</Link>
            <Link href="/processes" className="hover:underline">Processes</Link>
            <Link href="/compliance" className="hover:underline">Compliance</Link>
            <Link href="/automation" className="hover:underline">Automation</Link>
          </nav>
        </div>
      </header>

      <main className="flex-grow container mx-auto p-4">
        <h2 className="text-xl font-semibold mb-4">Dashboard Overview</h2>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="p-4 bg-white shadow rounded">
            <h3 className="text-gray-500">Total Processes Analyzed</h3>
            <p className="text-3xl font-bold">12</p>
          </div>
          <div className="p-4 bg-white shadow rounded">
            <h3 className="text-gray-500">Events Ingested</h3>
            <p className="text-3xl font-bold">45,230</p>
          </div>
          <div className="p-4 bg-white shadow rounded">
            <h3 className="text-gray-500">Bottlenecks Identified</h3>
            <p className="text-3xl font-bold">8</p>
          </div>
          <div className="p-4 bg-white shadow rounded">
            <h3 className="text-gray-500">Automation ROI</h3>
            <p className="text-3xl font-bold text-green-600">$125K</p>
          </div>
        </div>
      </main>
    </div>
  );
}
