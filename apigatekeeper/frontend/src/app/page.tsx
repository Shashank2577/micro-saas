"use client";

import { QueryClient, QueryClientProvider, useQuery } from "@tanstack/react-query";
import axios from "axios";
import { Activity, ShieldAlert, Zap, Globe } from "lucide-react";
import ReactECharts from "echarts-for-react";

const queryClient = new QueryClient();

const api = axios.create({
    baseURL: "http://localhost:8102",
    headers: {
        "X-Tenant-ID": "tenant-1"
    }
});

function Dashboard() {
    const { data: routes } = useQuery({
        queryKey: ["routes"],
        queryFn: () => api.get("/gateway/routes").then(res => res.data)
    });

    const { data: analytics } = useQuery({
        queryKey: ["analytics"],
        queryFn: () => api.get("/analytics/traffic").then(res => res.data)
    });

    const trafficData = {
        title: { text: "API Traffic Over Time" },
        tooltip: { trigger: "axis" },
        xAxis: { type: "category", data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"] },
        yAxis: { type: "value" },
        series: [{ data: [120, 200, 150, 80, 70, 110, 130], type: "line", smooth: true }]
    };

    const latencyData = {
        title: { text: "Latency Percentiles (ms)" },
        tooltip: { trigger: "axis" },
        xAxis: { type: "category", data: ["p50", "p95", "p99"] },
        yAxis: { type: "value" },
        series: [{ data: [15, 45, 120], type: "bar", color: "#6366f1" }]
    };

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-8">APIGatekeeper Dashboard</h1>

            <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
                <div className="bg-white p-6 rounded-lg shadow border border-gray-100">
                    <div className="flex items-center justify-between pb-2">
                        <h3 className="text-sm font-medium text-gray-500">Total Requests</h3>
                        <Activity className="h-4 w-4 text-gray-500" />
                    </div>
                    <div className="text-2xl font-bold">1,024,532</div>
                    <p className="text-xs text-gray-500">+20.1% from last month</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow border border-gray-100">
                    <div className="flex items-center justify-between pb-2">
                        <h3 className="text-sm font-medium text-gray-500">Active Routes</h3>
                        <Globe className="h-4 w-4 text-gray-500" />
                    </div>
                    <div className="text-2xl font-bold">{routes?.length || 0}</div>
                    <p className="text-xs text-gray-500">Current active configurations</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow border border-gray-100">
                    <div className="flex items-center justify-between pb-2">
                        <h3 className="text-sm font-medium text-gray-500">Error Rate</h3>
                        <ShieldAlert className="h-4 w-4 text-red-500" />
                    </div>
                    <div className="text-2xl font-bold text-red-600">0.12%</div>
                    <p className="text-xs text-gray-500">-0.05% from last week</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow border border-gray-100">
                    <div className="flex items-center justify-between pb-2">
                        <h3 className="text-sm font-medium text-gray-500">Avg Latency</h3>
                        <Zap className="h-4 w-4 text-yellow-500" />
                    </div>
                    <div className="text-2xl font-bold">45ms</div>
                    <p className="text-xs text-gray-500">p95 response time</p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
                <div className="bg-white p-6 rounded-lg shadow border border-gray-100">
                    <ReactECharts option={trafficData} style={{ height: "300px" }} />
                </div>
                <div className="bg-white p-6 rounded-lg shadow border border-gray-100">
                    <ReactECharts option={latencyData} style={{ height: "300px" }} />
                </div>
            </div>

            <div className="bg-white rounded-lg shadow border border-gray-100 overflow-hidden">
                <div className="p-6 border-b border-gray-100">
                    <h2 className="text-xl font-bold">Configured Routes</h2>
                </div>
                <div className="overflow-x-auto">
                    <table className="w-full text-sm text-left text-gray-500">
                        <thead className="text-xs text-gray-700 uppercase bg-gray-50">
                            <tr>
                                <th className="px-6 py-3">Route ID</th>
                                <th className="px-6 py-3">Path</th>
                                <th className="px-6 py-3">Method</th>
                                <th className="px-6 py-3">Target URL</th>
                            </tr>
                        </thead>
                        <tbody>
                            {routes?.length > 0 ? routes.map((route: any) => (
                                <tr key={route.id} className="bg-white border-b">
                                    <td className="px-6 py-4 font-medium text-gray-900">{route.routeId}</td>
                                    <td className="px-6 py-4">{route.path}</td>
                                    <td className="px-6 py-4">{route.method}</td>
                                    <td className="px-6 py-4">{route.targetUrl}</td>
                                </tr>
                            )) : (
                                <tr>
                                    <td colSpan={4} className="px-6 py-4 text-center">No routes configured</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <Dashboard />
        </QueryClientProvider>
    );
}
