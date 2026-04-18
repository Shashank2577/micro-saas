"use client";

import { useEffect, useState, useCallback } from 'react';
import { ReactFlow, Controls, Background, applyNodeChanges, applyEdgeChanges, NodeChange, EdgeChange, Edge, Node } from '@xyflow/react';
import '@xyflow/react/dist/style.css';
import api from '../../lib/api';

const initialNodes: Node[] = [
  { id: '1', position: { x: 0, y: 0 }, data: { label: 'Source: user_id' } },
  { id: '2', position: { x: 300, y: 0 }, data: { label: 'Target: customer_id' } },
];
const initialEdges: Edge[] = [{ id: 'e1-2', source: '1', target: '2' }];

export default function Mappings() {
  const [mappings, setMappings] = useState<any[]>([]);
  const [nodes, setNodes] = useState<Node[]>(initialNodes);
  const [edges, setEdges] = useState<Edge[]>(initialEdges);

  const onNodesChange = useCallback(
    (changes: NodeChange<Node>[]) => setNodes((nds) => applyNodeChanges(changes, nds)),
    [],
  );
  const onEdgesChange = useCallback(
    (changes: EdgeChange<Edge>[]) => setEdges((eds) => applyEdgeChanges(changes, eds)),
    [],
  );

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await api.get('/api/mappings');
        setMappings(response.data);
      } catch (e) {
        console.error(e);
      }
    }
    fetchData();
  }, []);

  async function addMapping() {
    try {
        await api.post('/api/mappings', {
            name: "Mock Schema Mapping",
            mappingRules: JSON.stringify({nodes, edges})
        });
        const response = await api.get('/api/mappings');
        setMappings(response.data);
    } catch (e) {
        console.error(e);
    }
  }

  return (
    <main className="p-8 h-screen">
      <h1 className="text-3xl font-bold mb-6">Schema Mappings</h1>
      <button onClick={addMapping} className="bg-blue-500 text-white p-2 mb-4 rounded">Save Mapping</button>
      
      <div className="h-1/2 border mb-8 bg-white">
        <ReactFlow nodes={nodes} edges={edges} onNodesChange={onNodesChange} onEdgesChange={onEdgesChange}>
            <Controls />
            <Background />
        </ReactFlow>
      </div>

      <div>
        <h2 className="text-xl mb-4">Saved Mappings</h2>
        <ul>
          {mappings.map(m => (
            <li key={m.id} className="border p-2 mb-2">
              {m.name} - Version: {m.version}
            </li>
          ))}
        </ul>
      </div>
    </main>
  );
}
