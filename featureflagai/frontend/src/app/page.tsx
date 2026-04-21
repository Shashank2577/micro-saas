import React from 'react';
import FlagList from '../components/FlagList';
import ImpactAnalysis from '../components/ImpactAnalysis';

export default function Dashboard() {
  return (
    <div className="p-8 font-sans">
      <h1 className="text-3xl font-bold mb-6">FeatureFlagAI Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <section>
          <h2 className="text-xl font-semibold mb-4">Manage Flags</h2>
          <FlagList />
        </section>
        <section>
          <h2 className="text-xl font-semibold mb-4">Impact Analysis</h2>
          <ImpactAnalysis />
        </section>
      </div>
    </div>
  );
}
