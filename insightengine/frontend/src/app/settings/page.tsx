"use client";

import { useEffect, useState } from 'react';
import { api } from '@/lib/api';

export default function SettingsPage() {
  const [rules, setRules] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [newRuleName, setNewRuleName] = useState('');
  const [newRuleDefinition, setNewRuleDefinition] = useState('{"metric": "revenue", "condition": ">", "value": 10000}');

  async function loadRules() {
    try {
      const data = await api.getCustomRules();
      setRules(data);
    } catch (error) {
      console.error("Failed to load custom rules", error);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadRules();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleCreateRule = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.createCustomRule({
        name: newRuleName,
        definition: newRuleDefinition,
        isActive: true
      });
      setShowForm(false);
      setNewRuleName('');
      loadRules();
    } catch (error) {
      console.error("Failed to create rule", error);
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">Settings</h1>

      <div className="bg-white border rounded-lg shadow-sm p-6 mb-8">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold text-gray-900">Custom Discovery Rules</h2>
          <button onClick={() => setShowForm(!showForm)} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
            {showForm ? 'Cancel' : 'Create Rule'}
          </button>
        </div>

        {showForm && (
          <div className="bg-gray-50 border rounded-lg p-6 mb-6">
            <h3 className="text-lg font-bold mb-4">New Custom Rule</h3>
            <form onSubmit={handleCreateRule} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Rule Name</label>
                <input type="text" required value={newRuleName} onChange={e => setNewRuleName(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" placeholder="Revenue Spike Rule" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Definition (JSON)</label>
                <textarea required value={newRuleDefinition} onChange={e => setNewRuleDefinition(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border font-mono text-sm" rows={4} />
              </div>
              <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Save Rule</button>
            </form>
          </div>
        )}
        {loading ? (
          <p className="text-gray-500">Loading rules...</p>
        ) : rules.length === 0 ? (
          <p className="text-gray-500">No custom discovery rules configured.</p>
        ) : (
          <table className="min-w-full divide-y divide-gray-200 mt-4">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Definition</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {rules.map((rule) => (
                <tr key={rule.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{rule.name}</td>
                  <td className="px-6 py-4 text-sm text-gray-500 font-mono text-xs overflow-hidden max-w-xs truncate">{rule.definition}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${rule.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                      {rule.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
