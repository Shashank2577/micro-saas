import { api, OnboardingPlan } from '@/lib/api';

export default async function DashboardPage() {
  let plans: OnboardingPlan[] = [];
  let analytics = { averageTimeToProductivity: 0 };

  try {
    const [fetchedPlans, fetchedAnalytics] = await Promise.all([
      api.plans.list(),
      api.analytics.getTimeToProductivity()
    ]);
    plans = fetchedPlans;
    analytics = fetchedAnalytics;
  } catch (e) {
    // Backend may not be running in build/static environments
  }

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">OnboardFlow</h1>
          <p className="text-gray-500 mt-1">AI employee onboarding automation</p>
        </div>

        {/* Stats row */}
        <div className="grid grid-cols-2 gap-4 mb-8">
          {[
            { label: 'Active Plans', value: plans.length },
            { label: 'Avg Time to Productivity', value: `${analytics.averageTimeToProductivity} days` },
          ].map((stat) => (
            <div key={stat.label} className="bg-white rounded-lg border border-gray-200 p-4">
              <p className="text-sm text-gray-500">{stat.label}</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">{stat.value}</p>
            </div>
          ))}
        </div>

        {/* Plans list */}
        <section className="mb-8">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Onboarding Plans</h2>
          {plans.length === 0 ? (
            <p className="text-gray-400 text-sm">No plans generated yet.</p>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
              {plans.map((plan) => (
                <div key={plan.id} className="bg-white rounded-lg border border-gray-200 p-4">
                  <h3 className="font-semibold text-gray-900">{plan.role}</h3>
                  <p className="text-sm text-gray-500">{plan.department}</p>
                  <p className="text-sm text-gray-500 mt-2">Status: {plan.status}</p>
                </div>
              ))}
            </div>
          )}
        </section>
      </div>
    </main>
  );
}
