"use client";

import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
import Link from 'next/link';

export default function AlertsPage() {
  const [rules, setRules] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [newRuleName, setNewRuleName] = useState('');
  const [newRuleCondition, setNewRuleCondition] = useState('ANOMALY_SEVERITY');
  const [newRuleThreshold, setNewRuleThreshold] = useState('0.8');

  async function loadRules() {
    try {
      const data = await api.getAlertRules();
      setRules(data);
    } catch (error) {
      console.error("Failed to load alert rules", error);
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
      await api.createAlertRule({
        name: newRuleName,
        conditionType: newRuleCondition,
        threshold: parseFloat(newRuleThreshold),
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
      <div className="flex justify-between items-center mb-6">
        <div>
          <Link href="/" className="text-blue-600 hover:underline mb-2 inline-block">&larr; Dashboard</Link>
          <h1 className="text-2xl font-bold text-gray-900">Alert Rules</h1>
        </div>
        <button onClick={() => setShowForm(!showForm)} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          {showForm ? 'Cancel' : 'Create Rule'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white border rounded-lg shadow-sm p-6 mb-6">
          <h2 className="text-xl font-bold mb-4">New Alert Rule</h2>
          <form onSubmit={handleCreateRule} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Rule Name</label>
              <input type="text" required value={newRuleName} onChange={e => setNewRuleName(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" placeholder="High Severity Anomaly" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Condition Type</label>
              <select value={newRuleCondition} onChange={e => setNewRuleCondition(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border">
                <option value="ANOMALY_SEVERITY">Anomaly Severity</option>
                <option value="TREND_ACCELERATION">Trend Acceleration</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Threshold</label>
              <input type="number" step="0.1" required value={newRuleThreshold} onChange={e => setNewRuleThreshold(e.target.value)} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" />
            </div>
            <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Save Rule</button>
          </form>
        </div>
      )}

      {loading ? (
        <div className="text-center py-10">Loading...</div>
      ) : rules.length === 0 ? (
        <div className="text-center py-10 text-gray-500">No alert rules configured.</div>
      ) : (
        <div className="bg-white border rounded-lg shadow-sm overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Condition</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Threshold</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {rules.map((rule) => (
                <tr key={rule.id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{rule.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{rule.conditionType}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{rule.threshold}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${rule.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                      {rule.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
