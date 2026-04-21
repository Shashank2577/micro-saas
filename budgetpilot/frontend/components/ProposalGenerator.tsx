"use client";
import React, { useState } from 'react';
import { proposeBudget } from '../lib/api';

export function ProposalGenerator() {
  const [department, setDepartment] = useState('');
  const [goals, setGoals] = useState('');
  const [proposal, setProposal] = useState('');
  const [loading, setLoading] = useState(false);

  const handleGenerate = async () => {
    setLoading(true);
    try {
      const result = await proposeBudget({ department, goals, fiscalYear: new Date().getFullYear(), historicalData: "See DB" });
      setProposal(result.proposalText);
    } catch (e) {
      console.error(e);
      setProposal('Error generating proposal.');
    }
    setLoading(false);
  };

  return (
    <div className="p-4 border rounded shadow-sm bg-white">
      <h3 className="text-lg font-bold mb-4">AI Budget Proposal</h3>
      <div className="mb-4">
        <label className="block mb-2 text-sm font-medium">Department</label>
        <input
          className="w-full border p-2 rounded"
          value={department}
          onChange={e => setDepartment(e.target.value)}
          placeholder="e.g. Engineering"
        />
      </div>
      <div className="mb-4">
        <label className="block mb-2 text-sm font-medium">Goals</label>
        <textarea
          className="w-full border p-2 rounded"
          value={goals}
          onChange={e => setGoals(e.target.value)}
          placeholder="e.g. Expand ML pipeline..."
        />
      </div>
      <button
        onClick={handleGenerate}
        disabled={loading}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        {loading ? 'Generating...' : 'Generate Proposal'}
      </button>
      {proposal && (
        <div className="mt-4 p-4 bg-gray-50 border rounded">
          <p className="whitespace-pre-wrap">{proposal}</p>
        </div>
      )}
    </div>
  );
}
