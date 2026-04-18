"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';
import Link from 'next/link';

export default function AutomationPage() {
  const [opportunities, setOpportunities] = useState<any[]>([]);

  useEffect(() => {
    const fetchOpps = async () => {
      try {
        const res = await api.get('/api/automation-opportunities');
        setOpportunities(res.data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchOpps();
  }, []);

  return (
    <div className="container mx-auto p-4">
      <Link href="/" className="text-blue-500 mb-4 inline-block">&larr; Back to Dashboard</Link>
      <h1 className="text-2xl font-bold mb-6">Automation Opportunities</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {opportunities.map(opp => (
          <div key={opp.id} className="bg-white p-5 shadow-lg rounded-lg border border-gray-200">
            <div className="flex justify-between items-start mb-4">
              <h3 className="font-bold text-lg text-gray-800">{opp.activityName}</h3>
              <span className={`text-xs px-2 py-1 rounded-full font-bold ${opp.effortEstimate === 'LOW' ? 'bg-green-100 text-green-800' : opp.effortEstimate === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' : 'bg-red-100 text-red-800'}`}>
                {opp.effortEstimate} EFFORT
              </span>
            </div>
            
            <div className="mb-4">
              <span className="text-sm text-gray-500 block">Estimated ROI</span>
              <span className="text-2xl font-black text-green-600">{opp.estimatedRoi}%</span>
            </div>
            
            <div className="bg-gray-50 p-3 rounded text-sm text-gray-700 italic">
              "{opp.rationale}"
            </div>
          </div>
        ))}
        {opportunities.length === 0 && <p className="col-span-3 text-center py-12 text-gray-500">No automation opportunities found. Run analysis on a process model to generate scores.</p>}
      </div>
    </div>
  );
}
