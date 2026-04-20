import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";

export default function BudgetComparison() {
    const [budgets, setBudgets] = useState([]);

    useEffect(() => {
        fetch('/api/budgets', { headers: { 'X-Tenant-ID': 'tenant-1' } })
            .then(res => res.json())
            .then(data => setBudgets(data))
            .catch(console.error);
    }, []);

    return (
        <Card>
            <CardHeader><CardTitle>Budget vs Actual</CardTitle></CardHeader>
            <CardContent>
                <ul>
                    {budgets.map((b: any) => (
                        <li key={b.id} className="py-2 border-b last:border-b-0 flex justify-between">
                            <span>{b.category?.name}</span>
                            <span>Target: ${b.amount}</span>
                        </li>
                    ))}
                    {budgets.length === 0 && <p className="text-gray-500">No budgets defined yet.</p>}
                </ul>
            </CardContent>
        </Card>
    );
}
