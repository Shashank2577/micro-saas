import React from 'react';
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

export default function ExpenseBreakdownPie() {
    return (
        <Card>
            <CardHeader><CardTitle>Expense Breakdown</CardTitle></CardHeader>
            <CardContent>
                <div className="h-64 bg-gray-100 flex items-center justify-center">
                    <p className="text-gray-500">Pie Chart Placeholder</p>
                </div>
            </CardContent>
        </Card>
    );
}
