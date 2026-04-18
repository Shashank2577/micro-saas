import Link from 'next/link';

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      <h1 className="text-4xl font-bold mb-8">NonprofitOS Dashboard</h1>
      <div className="flex gap-4">
        <Link href="/donors" className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
          Donors
        </Link>
        <Link href="/grants" className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
          Grants
        </Link>
        <Link href="/impacts" className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600">
          Impact
        </Link>
      </div>
    </main>
  );
}
