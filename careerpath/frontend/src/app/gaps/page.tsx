'use client';

import React, { useEffect, useState } from 'react';
import { api } from '@/lib/api';

const MOCK_EMP_ID = '00000000-0000-0000-0000-000000000001';

export default function GapsPage() {
  const [roles, setRoles] = useState([]);
  const [targetRoleId, setTargetRoleId] = useState('');
  const [gaps, setGaps] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.getRoles().then(setRoles).catch(console.error);
  }, []);

  const handleAnalyze = () => {
    if (!targetRoleId) return;
    setLoading(true);
    api.getSkillGaps(MOCK_EMP_ID, targetRoleId)
      .then(setGaps)
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-6">Skill Gaps Analysis</h1>
      <div className="mb-6 flex gap-4">
        <select 
          className="p-2 border rounded"
          value={targetRoleId}
          onChange={(e) => setTargetRoleId(e.target.value)}
        >
          <option value="">Select Target Role</option>
          {roles.map((r: any) => (
            <option key={r.id} value={r.id}>{r.title}</option>
          ))}
        </select>
        <button 
          className="bg-blue-600 text-white px-4 py-2 rounded"
          onClick={handleAnalyze}
          disabled={loading || !targetRoleId}
        >
          {loading ? 'Analyzing...' : 'Analyze Gaps'}
        </button>
      </div>

      {gaps.length > 0 && (
        <table className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100">
              <th className="p-3 border">Skill</th>
              <th className="p-3 border">Current</th>
              <th className="p-3 border">Required</th>
              <th className="p-3 border">Gap</th>
            </tr>
          </thead>
          <tbody>
            {gaps.map((g: any) => (
              <tr key={g.skillId}>
                <td className="p-3 border">{g.skillName}</td>
                <td className="p-3 border">{g.currentProficiency}</td>
                <td className="p-3 border">{g.requiredProficiency}</td>
                <td className="p-3 border text-red-600 font-bold">{g.gap}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {!loading && targetRoleId && gaps.length === 0 && (
        <p className="text-green-600">No skill gaps! You are well-prepared for this role.</p>
      )}
    </div>
  );
}
