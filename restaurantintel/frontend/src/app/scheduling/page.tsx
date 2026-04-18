"use client";

import { useState, useEffect } from 'react';
import api from '../../lib/api';

interface StaffSchedule {
  id: string;
  role: string;
  date: string;
  shiftStart: string;
  shiftEnd: string;
  predictedBusyness: string;
  recommendedStaffCount: number;
}

export default function StaffScheduling() {
  const [schedules, setSchedules] = useState<StaffSchedule[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchSchedules();
  }, []);

  const fetchSchedules = async () => {
    try {
      const res = await api.get('/api/v1/schedules');
      setSchedules(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const generateSchedules = async () => {
    setLoading(true);
    try {
      await api.post('/api/v1/schedules/generate');
      await fetchSchedules();
    } catch (err) {
      console.error(err);
      setLoading(false);
    }
  };

  if (loading) return <div className="p-8">Loading...</div>;

  return (
    <div className="p-8 max-w-5xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Staff Scheduling</h1>
        <button onClick={generateSchedules} className="bg-purple-600 text-white px-4 py-2 rounded">
          Generate AI Schedule
        </button>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Role</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Shift</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Busyness Level</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Recommended Staff</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {schedules.map(schedule => (
              <tr key={schedule.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{schedule.date}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{schedule.role}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{schedule.shiftStart} - {schedule.shiftEnd}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm">
                  <span className={`px-2 py-1 rounded text-xs font-semibold ${
                    schedule.predictedBusyness === 'HIGH' ? 'bg-red-100 text-red-800' :
                    schedule.predictedBusyness === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                    'bg-green-100 text-green-800'
                  }`}>
                    {schedule.predictedBusyness}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">{schedule.recommendedStaffCount}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {schedules.length === 0 && <p className="p-4 text-gray-500 text-center">No schedules generated yet.</p>}
      </div>
    </div>
  );
}
