'use client';
import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { api } from '@/lib/api';

export default function CallDetailPage() {
    const { id } = useParams() as { id: string };
    const [call, setCall] = useState<any>(null);
    const [transcript, setTranscript] = useState<any[]>([]);
    const [insights, setInsights] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        Promise.all([
            api.calls.get(id),
            fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8143'}/api/calls/${id}/transcript`, { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000001' }}).then(res => res.json()),
            fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8143'}/api/calls/${id}/insights`, { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000001' }}).then(res => res.json())
        ]).then(([callData, transcriptData, insightsData]) => {
            setCall(callData);
            setTranscript(transcriptData);
            setInsights(insightsData);
            setLoading(false);
        }).catch(err => {
            console.error(err);
            setLoading(false);
        });
    }, [id]);

    if (loading) return <div>Loading call details...</div>;
    if (!call) return <div>Call not found</div>;

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold">{call.title}</h1>
            <div className="bg-white p-4 rounded-lg shadow">
                <p><strong>Status:</strong> {call.status}</p>
                <p><strong>Summary:</strong> {call.summary || 'N/A'}</p>
            </div>

            <div className="grid grid-cols-3 gap-6">
                <div className="col-span-2 bg-white rounded-lg shadow p-4">
                    <h2 className="text-xl font-semibold mb-4">Transcript</h2>
                    <div className="space-y-4 max-h-[600px] overflow-y-auto">
                        {transcript.map((t, idx) => (
                            <div key={idx} className="flex flex-col">
                                <span className="text-sm font-semibold text-gray-500">{t.speaker?.speakerName || 'Unknown Speaker'} ({t.startTime}s - {t.endTime}s)</span>
                                <p className="text-gray-800">{t.text}</p>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="col-span-1 bg-white rounded-lg shadow p-4">
                    <h2 className="text-xl font-semibold mb-4">Insights</h2>
                    <div className="space-y-4">
                        {insights.map((insight, idx) => (
                            <div key={idx} className="p-3 bg-indigo-50 border border-indigo-100 rounded-md">
                                <span className="text-xs font-bold uppercase text-indigo-600">{insight.insightType}</span>
                                <h3 className="font-semibold text-gray-900">{insight.category}</h3>
                                <p className="text-sm text-gray-700">{insight.description}</p>
                            </div>
                        ))}
                        {insights.length === 0 && <p className="text-gray-500 text-sm">No insights extracted.</p>}
                    </div>
                </div>
            </div>
        </div>
    );
}
