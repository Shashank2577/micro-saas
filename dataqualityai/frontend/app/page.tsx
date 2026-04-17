import Link from 'next/link';

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col p-8">
      <h1 className="text-4xl font-bold mb-8">DataQualityAI</h1>
      <p className="text-xl mb-4">Catch bad data before it reaches a dashboard.</p>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
        <Link href="/datasets" className="p-6 border rounded-lg hover:bg-gray-50 transition-colors">
          <h2 className="text-2xl font-semibold mb-2">Datasets</h2>
          <p>Monitor your data assets for anomalies.</p>
        </Link>
        <Link href="/issues" className="p-6 border rounded-lg hover:bg-gray-50 transition-colors">
          <h2 className="text-2xl font-semibold mb-2">Issues</h2>
          <p>Review and resolve data quality alerts.</p>
        </Link>
      </div>
    </main>
  );
}
