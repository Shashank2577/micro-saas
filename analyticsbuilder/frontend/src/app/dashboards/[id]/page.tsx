"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { fetchApi } from "@/lib/api";
import "react-grid-layout/css/styles.css";
import "react-resizable/css/styles.css";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip as RechartsTooltip, Legend, ResponsiveContainer } from 'recharts';

const MOCK_DATA = [
  { name: 'Jan', uv: 4000, pv: 2400, amt: 2400 },
  { name: 'Feb', uv: 3000, pv: 1398, amt: 2210 },
  { name: 'Mar', uv: 2000, pv: 9800, amt: 2290 },
  { name: 'Apr', uv: 2780, pv: 3908, amt: 2000 },
  { name: 'May', uv: 1890, pv: 4800, amt: 2181 },
  { name: 'Jun', uv: 2390, pv: 3800, amt: 2500 },
  { name: 'Jul', uv: 3490, pv: 4300, amt: 2100 },
];

export default function DashboardPage() {
  const { id } = useParams();
  const [dashboard, setDashboard] = useState<any>(null);
  const [GridLayout, setGridLayout] = useState<any>(null);

  useEffect(() => {
    // Dynamic import to avoid SSR issues with react-grid-layout
    import("react-grid-layout").then((module) => {
      setGridLayout(() => module.default);
    });

    async function loadDashboard() {
      const data = await fetchApi(`/api/v1/dashboards/${id}`);
      setDashboard(data);
    }
    if (id) {
      loadDashboard();
    }
  }, [id]);

  if (!dashboard || !GridLayout) {
    return <div className="p-4">Loading...</div>;
  }

  const handleAddWidget = async () => {
    const newWidget = {
      type: "CHART",
      title: "Sample Bar Chart",
      config: "{}",
      positionX: 0,
      positionY: Infinity, // puts it at the bottom
      width: 6,
      height: 10,
    };

    const updatedWidgets = [...(dashboard.widgets || []), newWidget];
    const updatedDashboard = await fetchApi(`/api/v1/dashboards/${id}`, {
      method: "PUT",
      body: JSON.stringify({ widgets: updatedWidgets })
    });
    setDashboard(updatedDashboard);
  };

  const layout = dashboard.widgets?.map((widget: any, i: number) => ({
    i: widget.id || `temp-${i}`,
    x: widget.positionX || 0,
    y: widget.positionY || 0,
    w: widget.width || 4,
    h: widget.height || 4,
  })) || [];

  return (
    <div className="container mx-auto p-4 text-black">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold">{dashboard.name}</h1>
          <p className="text-gray-600 mt-1">{dashboard.description}</p>
        </div>
        <div>
          <button
            onClick={handleAddWidget}
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded shadow flex items-center gap-2"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" /></svg>
            Add Widget
          </button>
        </div>
      </div>

      <div className="bg-gray-50 border border-gray-200 rounded-lg p-4 min-h-[600px] overflow-x-auto">
        {layout.length > 0 ? (
          <div style={{ width: '1200px' }}>
            <GridLayout
              className="layout"
              layout={layout}
              cols={12}
              rowHeight={30}
              width={1200}
              isDraggable={true}
              isResizable={true}
            >
              {dashboard.widgets.map((widget: any, i: number) => (
                <div key={widget.id || `temp-${i}`} className="bg-white border rounded shadow p-4 flex flex-col h-full">
                  <h3 className="font-semibold text-lg border-b pb-2 mb-2 flex-shrink-0">{widget.title || "Widget"}</h3>
                  <div className="flex-grow w-full min-h-0">
                    {widget.type === "CHART" ? (
                      <ResponsiveContainer width="100%" height="100%">
                        <BarChart data={MOCK_DATA} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
                          <CartesianGrid strokeDasharray="3 3" />
                          <XAxis dataKey="name" />
                          <YAxis />
                          <RechartsTooltip />
                          <Legend />
                          <Bar dataKey="pv" fill="#8884d8" />
                          <Bar dataKey="uv" fill="#82ca9d" />
                        </BarChart>
                      </ResponsiveContainer>
                    ) : (
                      <div className="text-gray-500 text-sm">
                        Type: {widget.type}
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </GridLayout>
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center h-64 text-gray-400">
            <svg className="w-16 h-16 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
            </svg>
            <p>No widgets added to this dashboard yet. Click "Add Widget" to start building.</p>
          </div>
        )}
      </div>
    </div>
  );
}
