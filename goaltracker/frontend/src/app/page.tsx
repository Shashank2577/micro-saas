import Link from 'next/link';

export default function Home() {
  return (
    <div className="max-w-4xl mx-auto p-8">
      <h1 className="text-4xl font-bold mb-8">GoalTracker</h1>
      <p className="text-xl mb-8">Welcome to your financial goal setting and achievement platform.</p>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Link href="/goals/create" className="block p-6 bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow">
          <h2 className="text-2xl font-semibold mb-2 text-blue-600">Create Goal</h2>
          <p className="text-gray-600">Start tracking a new financial milestone.</p>
        </Link>
        <Link href="/goals" className="block p-6 bg-white rounded-lg shadow-sm hover:shadow-md transition-shadow">
          <h2 className="text-2xl font-semibold mb-2 text-blue-600">View Goals</h2>
          <p className="text-gray-600">Monitor your progress and milestones.</p>
        </Link>
      </div>
    </div>
  );
}
