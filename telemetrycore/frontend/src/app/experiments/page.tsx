"use client";

import { useEffect, useState } from 'react';
import api from '@/lib/api';

interface Experiment {
    id: string;
    name: string;
    variants: string[];
    allocationPercentage: number;
    status: string;
}

export default function ExperimentsPage() {
    const [experiments, setExperiments] = useState<Experiment[]>([]);
    const [results, setResults] = useState<Record<string, any>>({});

    useEffect(() => {
        api.get('/experiments').then(res => {
            setExperiments(res.data);
            res.data.forEach((exp: Experiment) => {
                api.get(`/experiments/${exp.id}/results`).then(rRes => {
                    setResults(prev => ({ ...prev, [exp.id]: rRes.data }));
                });
            });
        }).catch(console.error);
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">A/B Tests & Experiments</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {experiments.map(exp => (
                    <div key={exp.id} className="bg-white p-6 rounded-lg shadow-md border border-gray-200">
                        <div className="flex justify-between items-start mb-4">
                            <h2 className="text-xl font-semibold">{exp.name}</h2>
                            <span className="px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                                {exp.status}
                            </span>
                        </div>
                        <p className="text-sm text-gray-500 mb-4">Allocation: {exp.allocationPercentage}%</p>
                        
                        <div className="space-y-3">
                            <h3 className="text-sm font-medium text-gray-700">Results</h3>
                            {exp.variants.map(variant => (
                                <div key={variant} className="flex justify-between items-center bg-gray-50 p-3 rounded">
                                    <span className="font-medium text-gray-700">Variant: {variant}</span>
                                    <span className="font-bold text-blue-600">
                                        {results[exp.id]?.[variant]?.toFixed(2) || '0.00'}% conversion
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
