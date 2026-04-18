import { api, DataAsset, GovernancePolicy, ComplianceAudit } from '@/lib/api';

export default async function DashboardPage() {
  let assets: DataAsset[] = [];
  let policies: GovernancePolicy[] = [];
  let audits: ComplianceAudit[] = [];

  try {
    [assets, policies, audits] = await Promise.all([
      api.assets.list(),
      api.policies.list(),
      api.audits.list(),
    ]);
  } catch (error) {
    console.error("Backend fetch error:", error);
  }

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">DataGovernanceOS Dashboard</h1>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg border border-gray-200 shadow-sm">
            <h3 className="text-sm font-medium text-gray-500">Total Data Assets</h3>
            <p className="text-3xl font-bold text-gray-900 mt-2">{assets.length}</p>
          </div>
          <div className="bg-white p-6 rounded-lg border border-gray-200 shadow-sm">
            <h3 className="text-sm font-medium text-gray-500">Active Policies</h3>
            <p className="text-3xl font-bold text-gray-900 mt-2">{policies.length}</p>
          </div>
          <div className="bg-white p-6 rounded-lg border border-gray-200 shadow-sm">
            <h3 className="text-sm font-medium text-gray-500">Audit Failures</h3>
            <p className="text-3xl font-bold text-red-600 mt-2">
              {audits.filter((a: any) => a.status === 'FAILED').length}
            </p>
          </div>
        </div>

        <div className="bg-white rounded-lg border border-gray-200 shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200 bg-gray-50">
            <h2 className="text-lg font-medium text-gray-900">Recent Data Assets</h2>
          </div>
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Classification</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">PII</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {assets.map((asset: any) => (
                <tr key={asset.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{asset.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{asset.type}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <span className="px-2 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                      {asset.classification || 'PENDING'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {asset.piiStatus ? (
                      <span className="px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">Yes</span>
                    ) : (
                      <span className="px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">No</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </main>
  );
}
