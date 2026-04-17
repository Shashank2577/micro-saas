import { api } from '@/lib/api';

export default async function DashboardPage() {
  let projects: any[] = [];

  try {
    [projects] = await Promise.all([
      api.projects.list(),
    ]);
  } catch {
    // Backend may not be running in build/static environments
  }

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">ConstructionIQ</h1>
          <p className="text-gray-500 mt-1">Construction Management App</p>
        </div>

        {/* Stats row */}
        <div className="grid grid-cols-3 gap-4 mb-8">
          {[
            { label: 'Projects', value: projects.length },
          ].map((stat) => (
            <div key={stat.label} className="bg-white rounded-lg border border-gray-200 p-4">
              <p className="text-sm text-gray-500">{stat.label}</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">{stat.value}</p>
            </div>
          ))}
        </div>
      </div>
    </main>
  );
}
