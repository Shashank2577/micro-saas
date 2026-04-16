import React from 'react';

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center p-8">
      <div className="max-w-4xl w-full">
        <header className="mb-12 text-center">
          <h1 className="text-4xl font-bold text-gray-900 mb-4">CompBenchmark</h1>
          <p className="text-xl text-gray-600">AI real-time compensation benchmarking and pay equity analysis</p>
        </header>

        <div className="bg-white rounded-xl shadow-sm p-8">
          <h2 className="text-2xl font-semibold text-gray-800 mb-6">Dashboard</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="p-6 border border-gray-100 rounded-lg hover:shadow-md transition-shadow">
              <h3 className="text-lg font-medium text-gray-900 mb-2">Compensation Gaps</h3>
              <p className="text-gray-500 mb-4">Identify and address compensation gaps across your organization.</p>
              <button className="text-blue-600 font-medium hover:text-blue-700">View Analysis &rarr;</button>
            </div>
            <div className="p-6 border border-gray-100 rounded-lg hover:shadow-md transition-shadow">
              <h3 className="text-lg font-medium text-gray-900 mb-2">Pay Equity Audit</h3>
              <p className="text-gray-500 mb-4">Run comprehensive audits for pay equity by gender, ethnicity, and tenure.</p>
              <button className="text-blue-600 font-medium hover:text-blue-700">Run Audit &rarr;</button>
            </div>
            <div className="p-6 border border-gray-100 rounded-lg hover:shadow-md transition-shadow">
              <h3 className="text-lg font-medium text-gray-900 mb-2">Market Benchmarks</h3>
              <p className="text-gray-500 mb-4">Compare your compensation bands against real-time market data.</p>
              <button className="text-blue-600 font-medium hover:text-blue-700">Browse Data &rarr;</button>
            </div>
            <div className="p-6 border border-gray-100 rounded-lg hover:shadow-md transition-shadow">
              <h3 className="text-lg font-medium text-gray-900 mb-2">Offer Recommendations</h3>
              <p className="text-gray-500 mb-4">Get AI-driven salary range recommendations for new hires and promotions.</p>
              <button className="text-blue-600 font-medium hover:text-blue-700">Get Recommendation &rarr;</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
