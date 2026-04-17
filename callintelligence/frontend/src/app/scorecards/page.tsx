'use client';
import { useState } from 'react';
import { api } from '@/lib/api';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';

export default function ScorecardPage() {
    const [repId, setRepId] = useState('rep-1');
    const [scorecard, setScorecard] = useState<any>(null);
    const [loading, setLoading] = useState(false);

    const loadScorecard = async () => {
        setLoading(true);
        try {
            const data = await api.scorecards.get(repId);
            setScorecard(data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // Dummy data for chart since backend just gives aggregate ratio right now
    const chartData = [
        { name: 'Week 1', talkRatio: 65 },
        { name: 'Week 2', talkRatio: 58 },
        { name: 'Week 3', talkRatio: Math.round(scorecard?.averageTalkRatio || 50) }
    ];

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold">Rep Scorecard</h1>
            <div className="bg-white p-4 rounded-lg shadow flex items-center gap-4">
                <input 
                    type="text" 
                    value={repId} 
                    onChange={e => setRepId(e.target.value)} 
                    className="border p-2 rounded-md"
                    placeholder="Enter Rep ID"
                />
                <button onClick={loadScorecard} className="px-4 py-2 bg-indigo-600 text-white rounded-md">
                    Load Scorecard
                </button>
            </div>

            {loading && <div>Loading scorecard...</div>}
            
            {scorecard && !loading && (
                <div className="grid grid-cols-2 gap-6">
                    <div className="bg-white p-6 rounded-lg shadow border border-gray-200">
                        <h2 className="text-lg font-medium text-gray-900">Total Calls</h2>
                        <p className="mt-2 text-3xl font-bold text-indigo-600">{scorecard.totalCalls}</p>
                    </div>
                    <div className="bg-white p-6 rounded-lg shadow border border-gray-200">
                        <h2 className="text-lg font-medium text-gray-900">Avg Talk Ratio</h2>
                        <p className="mt-2 text-3xl font-bold text-indigo-600">{scorecard.averageTalkRatio}%</p>
                    </div>
                    
                    <div className="col-span-2 bg-white p-6 rounded-lg shadow border border-gray-200 h-80">
                        <h2 className="text-lg font-medium text-gray-900 mb-4">Talk Ratio Trend</h2>
                        <ResponsiveContainer width="100%" height="100%">
                            <BarChart data={chartData}>
                                <XAxis dataKey="name" />
                                <YAxis />
                                <Tooltip />
                                <Bar dataKey="talkRatio" fill="#4f46e5" />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>

                    <div className="col-span-2 bg-white p-6 rounded-lg shadow border border-gray-200">
                        <h2 className="text-lg font-medium text-gray-900 mb-4">Coaching Recommendations</h2>
                        <div className="space-y-4">
                            {scorecard.coachingRecommendations && scorecard.coachingRecommendations.map((rec: any, idx: number) => (
                                <div key={idx} className="p-4 bg-gray-50 rounded-md border border-gray-200">
                                    <h3 className="font-semibold text-gray-800">{rec.category}</h3>
                                    <p className="text-gray-600 mt-1">{rec.recommendation}</p>
                                    {rec.playbookSuggestion && (
                                        <div className="mt-2 text-sm bg-indigo-50 text-indigo-800 p-2 rounded inline-block">
                                            <strong>Playbook:</strong> {rec.playbookSuggestion}
                                        </div>
                                    )}
                                </div>
                            ))}
                            {(!scorecard.coachingRecommendations || scorecard.coachingRecommendations.length === 0) && (
                                <p className="text-gray-500">No recommendations available.</p>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
