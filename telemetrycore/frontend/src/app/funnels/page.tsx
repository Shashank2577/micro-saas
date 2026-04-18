"use client";

import { useEffect, useState } from 'react';
import api from '@/lib/api';

interface Funnel {
    id: string;
    name: string;
    steps: string[];
}

export default function FunnelsPage() {
    const [funnels, setFunnels] = useState<Funnel[]>([]);
    const [analysis, setAnalysis] = useState<Record<string, any>>({});

    useEffect(() => {
        api.get('/funnels').then(res => {
            setFunnels(res.data);
            res.data.forEach((funnel: Funnel) => {
                api.get(`/funnels/${funnel.id}/analysis`).then(aRes => {
                    setAnalysis(prev => ({ ...prev, [funnel.id]: aRes.data }));
                });
            });
        }).catch(console.error);
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Funnels</h1>
            <div className="space-y-8">
                {funnels.map(funnel => (
                    <div key={funnel.id} className="bg-white p-6 rounded-lg shadow border border-gray-200">
                        <h2 className="text-xl font-semibold mb-4">{funnel.name}</h2>
                        <div className="flex space-x-4 items-center">
                            {funnel.steps.map((step, index) => (
                                <div key={step} className="flex items-center">
                                    <div className="bg-blue-50 text-blue-700 px-4 py-2 rounded">
                                        <p className="font-medium">{step}</p>
                                        <p className="text-sm font-bold mt-1">
                                            {analysis[funnel.id]?.[step]}%
                                        </p>
                                    </div>
                                    {index < funnel.steps.length - 1 && (
                                        <div className="mx-4 text-gray-400">→</div>
                                    )}
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
