import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

export default function RecommendationList() {
    const [recommendations, setRecommendations] = useState([]);

    useEffect(() => {
        fetch('/api/optimization/recommendations', { headers: { 'X-Tenant-ID': 'tenant-1' } })
            .then(res => res.json())
            .then(data => setRecommendations(data))
            .catch(console.error);
    }, []);

    return (
        <Card>
            <CardHeader><CardTitle>Optimization Recommendations</CardTitle></CardHeader>
            <CardContent>
                <ul className="space-y-4">
                    {recommendations.map((r: any) => (
                        <li key={r.id} className="p-4 border rounded shadow-sm">
                            <h4 className="font-semibold">{r.type}</h4>
                            <p className="text-sm text-gray-600">{r.description}</p>
                            <p className="text-green-600 font-bold mt-2">Potential Savings: ${r.potentialSavings}</p>
                        </li>
                    ))}
                    {recommendations.length === 0 && <p className="text-gray-500">No recommendations currently available.</p>}
                </ul>
            </CardContent>
        </Card>
    );
}
