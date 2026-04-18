import Link from 'next/link';

export default function Home() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">CareerPath</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <Link href="/roadmap" className="p-6 bg-white border rounded-lg shadow-sm hover:shadow-md transition">
          <h2 className="text-xl font-semibold mb-2">Career Roadmap</h2>
          <p className="text-gray-600">Visualize roles and progression paths.</p>
        </Link>
        <Link href="/skills" className="p-6 bg-white border rounded-lg shadow-sm hover:shadow-md transition">
          <h2 className="text-xl font-semibold mb-2">My Skills</h2>
          <p className="text-gray-600">View and update your skill inventory.</p>
        </Link>
        <Link href="/gaps" className="p-6 bg-white border rounded-lg shadow-sm hover:shadow-md transition">
          <h2 className="text-xl font-semibold mb-2">Skill Gaps</h2>
          <p className="text-gray-600">Analyze skill gaps for target roles.</p>
        </Link>
        <Link href="/recommendations" className="p-6 bg-white border rounded-lg shadow-sm hover:shadow-md transition">
          <h2 className="text-xl font-semibold mb-2">Role Recommendations</h2>
          <p className="text-gray-600">AI-suggested next career moves.</p>
        </Link>
        <Link href="/learning" className="p-6 bg-white border rounded-lg shadow-sm hover:shadow-md transition">
          <h2 className="text-xl font-semibold mb-2">Learning Paths</h2>
          <p className="text-gray-600">Curated courses and projects.</p>
        </Link>
        <Link href="/mentorship" className="p-6 bg-white border rounded-lg shadow-sm hover:shadow-md transition">
          <h2 className="text-xl font-semibold mb-2">Mentorship</h2>
          <p className="text-gray-600">Find mentors and view guidance.</p>
        </Link>
        <Link href="/development-plan" className="p-6 bg-white border rounded-lg shadow-sm hover:shadow-md transition">
          <h2 className="text-xl font-semibold mb-2">Development Plan</h2>
          <p className="text-gray-600">Generated milestones and timelines.</p>
        </Link>
      </div>
    </div>
  );
}
