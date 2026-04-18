"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';
import { Cohort } from '../../types';
import Link from 'next/link';

export default function CohortsPage() {
  const [cohorts, setCohorts] = useState<Cohort[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({ name: '', description: '', criteria: '{}' });

  useEffect(() => {
    fetchCohorts();
  }, []);

  async function fetchCohorts() {
    try {
      const res = await api.get<Cohort[]>('/cohorts');
      setCohorts(res.data);
    } catch (error) {
      console.error('Error fetching cohorts:', error);
    } finally {
      setLoading(false);
    }
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      const payload = {
        ...formData,
        criteria: JSON.parse(formData.criteria)
      };
      await api.post('/cohorts', payload);
      setShowForm(false);
      setFormData({ name: '', description: '', criteria: '{}' });
      fetchCohorts();
    } catch (error) {
      console.error('Error creating cohort:', error);
      alert('Failed to create cohort. Check criteria JSON.');
    }
  }

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Cohorts</h1>
        <div className="space-x-4">
          <button onClick={() => setShowForm(!showForm)} className="bg-blue-600 text-white px-4 py-2 rounded">
            {showForm ? 'Cancel' : 'Create Cohort'}
          </button>
          <Link href="/" className="text-blue-600 hover:underline">Dashboard</Link>
        </div>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="bg-white p-6 shadow rounded-lg space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Name</label>
            <input required type="text" value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Description</label>
            <input type="text" value={formData.description} onChange={e => setFormData({...formData, description: e.target.value})} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Criteria (JSON)</label>
            <textarea required value={formData.criteria} onChange={e => setFormData({...formData, criteria: e.target.value})} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border" rows={3}></textarea>
          </div>
          <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded">Save Cohort</button>
        </form>
      )}

      {loading ? (
        <p>Loading cohorts...</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {cohorts.map((cohort) => (
            <div key={cohort.id} className="bg-white p-6 shadow rounded-lg">
              <h3 className="text-xl font-bold">{cohort.name}</h3>
              <p className="text-gray-600 mt-2">{cohort.description}</p>
              <div className="mt-4">
                <h4 className="text-sm font-semibold text-gray-500">Criteria:</h4>
                <pre className="text-xs bg-gray-50 p-2 rounded mt-1 overflow-auto">{JSON.stringify(cohort.criteria, null, 2)}</pre>
              </div>
            </div>
          ))}
          {cohorts.length === 0 && <p className="text-gray-500">No cohorts found.</p>}
        </div>
      )}
    </div>
  );
}
