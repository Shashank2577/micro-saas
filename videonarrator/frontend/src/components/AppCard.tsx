import { EcosystemApp } from '@/lib/api';

interface Props {
  app: EcosystemApp;
}

export function AppCard({ app }: Props) {
  const statusColor: Record<string, string> = {
    ACTIVE: 'bg-green-100 text-green-800',
    INACTIVE: 'bg-gray-100 text-gray-600',
    ERROR: 'bg-red-100 text-red-800',
  };

  return (
    <div className="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
      <div className="flex items-start justify-between mb-2">
        <div>
          <h3 className="font-semibold text-gray-900">{app.displayName}</h3>
          <p className="text-sm text-gray-500">{app.name}</p>
        </div>
        <span className={`text-xs font-medium px-2 py-1 rounded-full ${statusColor[app.status]}`}>
          {app.status}
        </span>
      </div>
      <p className="text-xs text-gray-400 truncate">{app.baseUrl}</p>
      {app.lastHeartbeatAt && (
        <p className="text-xs text-gray-400 mt-1">
          Last seen: {new Date(app.lastHeartbeatAt).toLocaleString()}
        </p>
      )}
    </div>
  );
}
