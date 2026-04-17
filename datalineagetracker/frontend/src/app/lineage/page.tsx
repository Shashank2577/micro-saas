"use client";

import { useEffect, useState, Suspense } from 'react';
import { useSearchParams } from 'next/navigation';
import ReactFlow, { Background, Controls, Node, Edge } from 'reactflow';
import 'reactflow/dist/style.css';
import { LineageLink } from '@/types';

function LineageGraph() {
    const searchParams = useSearchParams();
    const assetId = searchParams.get('assetId');
    
    const [nodes, setNodes] = useState<Node[]>([]);
    const [edges, setEdges] = useState<Edge[]>([]);

    useEffect(() => {
        // Fetch all links for simplicity in this demo
        fetch('http://localhost:8167/api/v1/lineage', {
            headers: {
                'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
            }
        })
        .then(res => res.json())
        .then((links: LineageLink[]) => {
            const newNodes: Node[] = [];
            const newEdges: Edge[] = [];
            const addedNodes = new Set<string>();

            let x = 100;
            let y = 100;

            links.forEach(link => {
                if (link.sourceAsset && !addedNodes.has(link.sourceAsset.id)) {
                    newNodes.push({
                        id: link.sourceAsset.id,
                        position: { x, y: y },
                        data: { label: `${link.sourceAsset.name} (${link.sourceAsset.type})` },
                        style: { background: '#e0e7ff', border: '1px solid #4f46e5', borderRadius: '4px', padding: '10px' }
                    });
                    addedNodes.add(link.sourceAsset.id);
                    x += 250;
                }
                
                if (link.targetAsset && !addedNodes.has(link.targetAsset.id)) {
                    newNodes.push({
                        id: link.targetAsset.id,
                        position: { x, y: y + 100 },
                        data: { label: `${link.targetAsset.name} (${link.targetAsset.type})` },
                        style: { background: '#fce7f3', border: '1px solid #db2777', borderRadius: '4px', padding: '10px' }
                    });
                    addedNodes.add(link.targetAsset.id);
                    x += 250;
                }

                if (link.sourceAsset && link.targetAsset) {
                    newEdges.push({
                        id: link.id,
                        source: link.sourceAsset.id,
                        target: link.targetAsset.id,
                        animated: true,
                        label: link.transformationLogic ? 'Transform' : ''
                    });
                }
            });

            setNodes(newNodes);
            setEdges(newEdges);
        })
        .catch(console.error);
    }, [assetId]);

    return (
        <div style={{ height: '80vh', width: '100%' }}>
            <ReactFlow nodes={nodes} edges={edges} fitView>
                <Background />
                <Controls />
            </ReactFlow>
        </div>
    );
}

export default function LineagePage() {
    return (
        <div className="max-w-7xl mx-auto p-8">
            <h1 className="text-2xl font-bold mb-6">Interactive Lineage Graph</h1>
            <div className="bg-white border shadow rounded-lg p-4">
                <Suspense fallback={<div>Loading Graph...</div>}>
                    <LineageGraph />
                </Suspense>
            </div>
        </div>
    );
}
