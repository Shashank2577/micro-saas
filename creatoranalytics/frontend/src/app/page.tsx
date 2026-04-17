"use client";

import { useEffect, useState } from "react";

export default function CreatorAnalyticsDashboard() {
  const [channels, setChannels] = useState<any[]>([]);
  const [insights, setInsights] = useState<any[]>([]);
  const [roi, setRoi] = useState({ totalValue: 0, totalViews: 0, valuePerView: 0 });
  const tenantId = "123e4567-e89b-12d3-a456-426614174000"; // Mock tenant ID

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const headers = { "X-Tenant-ID": tenantId };
      const [channelsRes, insightsRes, roiRes] = await Promise.all([
        fetch("http://localhost:8136/api/v1/channels", { headers }),
        fetch("http://localhost:8136/api/v1/insights", { headers }),
        fetch("http://localhost:8136/api/v1/analytics/roi", { headers }),
      ]);

      setChannels(await channelsRes.json());
      setInsights(await insightsRes.json());
      setRoi(await roiRes.json());
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-8">Creator Analytics Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="p-6 bg-white rounded-lg shadow">
          <h3 className="text-gray-500 text-sm font-medium">Total Value Attributed</h3>
          <p className="text-3xl font-bold mt-2">${roi.totalValue}</p>
        </div>
        <div className="p-6 bg-white rounded-lg shadow">
          <h3 className="text-gray-500 text-sm font-medium">Total Views</h3>
          <p className="text-3xl font-bold mt-2">{roi.totalViews}</p>
        </div>
        <div className="p-6 bg-white rounded-lg shadow">
          <h3 className="text-gray-500 text-sm font-medium">Value per View</h3>
          <p className="text-3xl font-bold mt-2">${roi.valuePerView}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div>
          <h2 className="text-xl font-bold mb-4">Content Channels</h2>
          <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Platform</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tracked Since</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {channels.map((channel: any) => (
                  <tr key={channel.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{channel.name}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{channel.platform}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{channel.trackedSince}</td>
                  </tr>
                ))}
                {channels.length === 0 && (
                  <tr>
                    <td colSpan={3} className="px-6 py-4 text-center text-sm text-gray-500">No channels found</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        <div>
          <h2 className="text-xl font-bold mb-4">AI Insights</h2>
          <div className="space-y-4">
            {insights.map((insight: any) => (
              <div key={insight.id} className="p-6 bg-blue-50 rounded-lg border border-blue-100">
                <div className="flex items-center mb-2">
                  <span className="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded">
                    {insight.insightType}
                  </span>
                </div>
                <p className="text-gray-800">{insight.insightText}</p>
                <div className="mt-3 text-xs text-gray-500">
                  Channel ID: {insight.channelId}
                </div>
              </div>
            ))}
            {insights.length === 0 && (
              <div className="p-6 bg-gray-50 rounded-lg text-center text-gray-500">
                No insights available
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
