import Link from 'next/link';

export default function Home() {
  return (
    <main className="p-8">
      <h1 className="text-3xl font-bold mb-6">Performance Narrative</h1>
      <p className="mb-4">Welcome to the Performance Narrative ecosystem application.</p>
      <Link href="/performance" className="text-blue-600 hover:underline">
        Go to Dashboard
      </Link>
    </main>
  );
}
