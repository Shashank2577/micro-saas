"use client";

import React, { useEffect, useState } from 'react';
import api from '../../lib/api';
import { formatDistanceToNow } from 'date-fns';
import { Clock, Play, Pause } from 'lucide-react';

export default function ScheduledPage() {
  const [tasks, setTasks] = useState<any[]>([]);

  const fetchTasks = async () => {
    try {
      const { data } = await api.get('/scheduled-tasks');
      setTasks(data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const createSampleCron = async () => {
    try {
      await api.post('/scheduled-tasks', {
        name: 'Daily Report',
        cronExpression: '0 0 * * *',
        jobName: 'generate_report',
        payloadTemplate: '{"type":"daily"}'
      });
      fetchTasks();
    } catch (err) {
      console.error(err);
    }
  };

  const toggleTask = async (id: string) => {
    try {
      await api.put(`/scheduled-tasks/${id}/toggle`);
      fetchTasks();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold">Scheduled Tasks</h2>
        <button onClick={createSampleCron} className="bg-blue-600 text-white px-4 py-2 rounded shadow hover:bg-blue-700">Add Sample Cron</button>
      </div>

      <div className="bg-white shadow rounded-lg overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Schedule</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Job Target</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {tasks.map(task => (
              <tr key={task.id}>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm font-medium text-gray-900">{task.name}</div>
                  <div className="text-xs text-gray-500">Created {formatDistanceToNow(new Date(task.createdAt), { addSuffix: true })}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  <Clock className="w-4 h-4 inline mr-1" />{task.cronExpression}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{task.jobName}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${task.active ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                    {task.active ? 'Active' : 'Paused'}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button onClick={() => toggleTask(task.id)} className={`${task.active ? 'text-yellow-600 hover:text-yellow-900' : 'text-green-600 hover:text-green-900'}`}>
                    {task.active ? <><Pause className="w-4 h-4 inline" /> Pause</> : <><Play className="w-4 h-4 inline" /> Resume</>}
                  </button>
                </td>
              </tr>
            ))}
            {tasks.length === 0 && (
              <tr>
                <td colSpan={5} className="px-6 py-4 text-center text-gray-500">No scheduled tasks found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
