import Link from 'next/link';

export function Header() {
  return (
    <header className="bg-white shadow">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <h1 className="text-xl font-bold text-gray-900">CopyOptimizer</h1>
        <nav className="flex space-x-4">
          <Link href="/assets" className="text-gray-600 hover:text-gray-900">Assets</Link>
          <Link href="/variants" className="text-gray-600 hover:text-gray-900">Variants</Link>
          <Link href="/scoring" className="text-gray-600 hover:text-gray-900">Scoring</Link>
          <Link href="/experiments" className="text-gray-600 hover:text-gray-900">Experiments</Link>
          <Link href="/promotion" className="text-gray-600 hover:text-gray-900">Promotion</Link>
          <Link href="/analytics" className="text-gray-600 hover:text-gray-900">Analytics</Link>
        </nav>
      </div>
    </header>
  );
}
