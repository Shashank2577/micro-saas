import { AppCard } from '../components/AppCard';
import { api } from '../lib/api';

// Adding this to force dynamic rendering so that the fetch failure on build time won't block deployment
export const dynamic = 'force-dynamic';

export default async function DashboardPage() {
  let apps: any[] = [];
  let events: any[] = [];
  let workflows: any[] = [];

  try {
    const data = await api.apps.list();
    apps = data || [];
  } catch (error) {
    console.error('Failed to load apps:', error);
  }

  try {
    const data = await api.events.list();
    events = data || [];
  } catch (error) {
    console.error('Failed to load events:', error);
  }

  try {
    const data = await api.workflows.list();
    workflows = data || [];
  } catch (error) {
    console.error('Failed to load workflows:', error);
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-slate-900">Nexus Hub</h1>
        <p className="text-slate-500 mt-2">Ecosystem Orchestrator</p>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {apps.map((app: any) => (
          <AppCard key={app.id} app={app} />
        ))}
      </div>
    </div>
  );
}
