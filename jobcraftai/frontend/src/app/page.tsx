import { useState } from 'react';

export default function JobCraftAIDashboard() {
  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">JobCraftAI</h1>
          <p className="text-gray-500 mt-1">AI bias-checked SEO-optimized job description generation platform</p>
        </div>

        {/* Placeholder for Job Descriptions List */}
        <section className="mb-8">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Job Descriptions</h2>
          <p className="text-gray-400 text-sm">No job descriptions generated yet.</p>
        </section>
      </div>
    </main>
  );
}
