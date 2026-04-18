'use client';

import React, { useEffect, useState } from 'react';
import { api } from '@/lib/api';

const MOCK_EMP_ID = '00000000-0000-0000-0000-000000000001';

export default function SkillsPage() {
  const [skills, setSkills] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getSkills(MOCK_EMP_ID)
      .then(data => setSkills(data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-6">My Skills</h1>
      {loading ? <p>Loading...</p> : (
        <table className="w-full border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100">
              <th className="p-3 border">Skill Name</th>
              <th className="p-3 border">Current Proficiency (1-5)</th>
            </tr>
          </thead>
          <tbody>
            {skills.map((s: any) => (
              <tr key={s.skillId}>
                <td className="p-3 border">{s.skillName}</td>
                <td className="p-3 border">{s.currentProficiency}</td>
              </tr>
            ))}
            {skills.length === 0 && (
              <tr>
                <td colSpan={2} className="p-3 border text-center">No skills recorded yet.</td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
