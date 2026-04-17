import Link from 'next/link';

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <h1 className="text-4xl font-bold mb-4">DebtNavigator</h1>
      <p className="text-xl text-gray-600 mb-8">Optimize your path to becoming debt-free.</p>

      <div className="flex gap-4">
        <Link
          href="/debts"
          className="bg-blue-600 text-white px-6 py-3 rounded-lg font-medium hover:bg-blue-700 transition"
        >
          Manage Debts
        </Link>
        <Link
          href="/strategies"
          className="bg-green-600 text-white px-6 py-3 rounded-lg font-medium hover:bg-green-700 transition"
        >
          Payoff Strategies
        </Link>
      </div>
    </div>
  );
}
