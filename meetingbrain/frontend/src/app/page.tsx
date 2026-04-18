"use client";

import React, { useEffect, useState } from 'react';
import api from '../lib/api';

export default function Home() {
    const [stats, setStats] = useState({
        totalMeetings: 0,
        openActionItems: 0,
        recentDecisions: 0
    });
    const [meetings, setMeetings] = useState([]);

    useEffect(() => {
        // Fetch dashboard data
        const fetchData = async () => {
            try {
                const res = await api.get('/api/meetings');
                setMeetings(res.data.content || []);
            } catch (err) {
                console.error("Failed to fetch meetings", err);
            }
        };
        fetchData();
    }, []);

    return (
        <div className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-6">MeetingBrain Dashboard</h1>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-lg font-semibold text-gray-700">Total Meetings</h3>
                    <p className="text-3xl font-bold mt-2">{meetings.length}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-lg font-semibold text-gray-700">Open Action Items</h3>
                    <p className="text-3xl font-bold mt-2">{stats.openActionItems}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h3 className="text-lg font-semibold text-gray-700">Recent Decisions</h3>
                    <p className="text-3xl font-bold mt-2">{stats.recentDecisions}</p>
                </div>
            </div>

            <h2 className="text-2xl font-bold mb-4">Recent Meetings</h2>
            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Platform</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {meetings.map((meeting: any) => (
                            <tr key={meeting.id}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-blue-600">
                                    <a href={`/meetings/${meeting.id}`}>{meeting.title}</a>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {new Date(meeting.startTime).toLocaleDateString()}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{meeting.platform}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{meeting.status}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
