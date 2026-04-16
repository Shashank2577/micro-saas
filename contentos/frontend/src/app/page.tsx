import Link from 'next/link';

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 p-8">
      <h1 className="text-4xl font-bold text-gray-900 mb-8">ContentOS</h1>
      <p className="text-xl text-gray-600 mb-12">AI content operations platform for content teams</p>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-4xl w-full">
        <Link href="/content" className="block p-6 bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow">
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Content Items</h2>
          <p className="text-gray-600">Manage your blog posts, newsletters, social media, and more.</p>
        </Link>

        <Link href="/calendar" className="block p-6 bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow">
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Content Calendar</h2>
          <p className="text-gray-600">Schedule and plan your content publishing across all channels.</p>
        </Link>

        <Link href="/briefs" className="block p-6 bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow">
          <h2 className="text-2xl font-bold text-gray-900 mb-2">AI Content Briefs</h2>
          <p className="text-gray-600">Generate strategic content briefs with AI to guide your writers.</p>
        </Link>
      </div>
    </div>
  );
}
