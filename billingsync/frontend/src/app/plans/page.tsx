'use client';

export default function PlansPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Subscription Plans</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="p-6 bg-white shadow rounded-lg border border-gray-200">
          <h2 className="text-xl font-bold mb-2">Basic Tier</h2>
          <p className="text-gray-600 mb-4">$10 / month</p>
          <ul className="list-disc pl-5 text-sm text-gray-700">
            <li>1,000 API Calls included</li>
            <li>$0.01 / extra call</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
