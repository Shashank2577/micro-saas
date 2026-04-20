import React from 'react';
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

export default function CashFlowChart() {
    return (
        <Card>
            <CardHeader><CardTitle>Cash Flow Trend</CardTitle></CardHeader>
            <CardContent>
                <div className="h-64 bg-gray-100 flex items-center justify-center">
                    <p className="text-gray-500">Chart Visualization Placeholder</p>
                </div>
            </CardContent>
        </Card>
    );
}
