'use client';

export default function ReportsPage() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Revenue Reports & Optimization</h1>
      
      <div className="p-6 bg-blue-50 border border-blue-200 rounded-lg mb-8">
        <h2 className="text-lg font-bold text-blue-900 mb-2">AI Recommendation</h2>
        <p className="text-blue-800">Your MRR is growing. Focus on volume-based pricing discounts to encourage higher usage among enterprise clients.</p>
      </div>

      <div className="p-6 bg-white shadow rounded-lg border border-gray-200">
        <h2 className="text-xl font-bold mb-4">Monthly Revenue</h2>
        <div className="h-64 bg-gray-100 flex items-center justify-center text-gray-500 rounded">
          [Chart Placeholder: Revenue over time]
        </div>
      </div>
    </div>
  );
}
