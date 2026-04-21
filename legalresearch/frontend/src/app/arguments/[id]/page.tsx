"use client";

import React, { useEffect, useState } from 'react';
import { api } from '../../../lib/api';
import ArgumentNode from '../../../components/ArgumentNode';

export default function ArgumentGraphPage({ params }: { params: { id: string } }) {
  const [graph, setGraph] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchGraph = async () => {
      try {
        const data = await api.getArgumentGraph(params.id);
        setGraph(data);
      } catch (error) {
        console.error("Error fetching argument graph", error);
      } finally {
        setLoading(false);
      }
    };
    fetchGraph();
  }, [params.id]);

  if (loading) return <div>Loading argument graph...</div>;
  if (!graph) return <div>Graph not found.</div>;

  return (
    <div className="space-y-6 h-full flex flex-col">
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow">
        <h1 className="text-2xl font-bold">Argument Graph</h1>
      </div>

      <div className="bg-white p-6 rounded-lg shadow flex-1 overflow-auto relative min-h-[500px]">
        {graph.nodes && graph.nodes.length > 0 ? (
          <div className="flex flex-wrap gap-8 justify-center items-center">
            {/* Simple visual representation without complex canvas/svg positioning */}
            {graph.nodes.map((node: any) => (
              <ArgumentNode key={node.id} data={node} />
            ))}
          </div>
        ) : (
          <p className="text-gray-500 text-center mt-10">No nodes in this argument graph.</p>
        )}
      </div>
    </div>
  );
}
