import { api } from '@/lib/api';
import { AppCard } from '@/components/AppCard';

export default async function DashboardPage() {
  let apps: any[] = [];
  let events: any[] = [];
  let workflows: any[] = [];

  try {
    [apps, events, workflows] = await Promise.all([
      api.apps.list(),
      api.events.list(10),
      api.workflows.list(),
    ]);
  } catch {
    // Backend may not be running in build/static environments
  }

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Nexus Hub</h1>
          <p className="text-gray-500 mt-1">Ecosystem orchestrator — {apps.length} apps connected</p>
        </div>

        {/* Stats row */}
        <div className="grid grid-cols-3 gap-4 mb-8">
          {[
            { label: 'Connected Apps', value: apps.length },
            { label: 'Active Workflows', value: workflows.filter((w) => w.enabled).length },
            { label: 'Events (last 10)', value: events.length },
          ].map((stat) => (
            <div key={stat.label} className="bg-white rounded-lg border border-gray-200 p-4">
              <p className="text-sm text-gray-500">{stat.label}</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">{stat.value}</p>
            </div>
          ))}
        </div>

        {/* Apps grid */}
        <section className="mb-8">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Connected Apps</h2>
          {apps.length === 0 ? (
            <p className="text-gray-400 text-sm">No apps registered yet. Use the API to register your first app.</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
              {apps.map((app) => <AppCard key={app.id} app={app} />)}
            </div>
          )}
        </section>

        {/* Recent events */}
        <section>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Recent Events</h2>
          {events.length === 0 ? (
            <p className="text-gray-400 text-sm">No events yet.</p>
          ) : (
            <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Source</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Time</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {events.map((event) => (
                    <tr key={event.id}>
                      <td className="px-4 py-2 text-sm text-gray-900">{event.sourceApp}</td>
                      <td className="px-4 py-2 text-sm text-gray-600">{event.eventType}</td>
                      <td className="px-4 py-2 text-sm text-gray-400">
                        {new Date(event.createdAt).toLocaleString()}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </main>
  );
}
