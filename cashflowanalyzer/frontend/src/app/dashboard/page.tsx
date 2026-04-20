"use client";

import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import CashFlowChart from "@/components/CashFlowChart";
import RecommendationList from "@/components/RecommendationList";

export default function DashboardPage() {
    const [summary, setSummary] = useState({ totalIncome: 0, totalExpenses: 0, savingsRate: 0 });

    useEffect(() => {
        fetch('/api/analytics/cash-flow', { headers: { 'X-Tenant-ID': 'tenant-1' } })
            .then(res => res.json())
            .then(data => setSummary(data))
            .catch(console.error);
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Dashboard</h1>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                <Card>
                    <CardHeader><CardTitle>Total Income</CardTitle></CardHeader>
                    <CardContent className="text-2xl">${summary.totalIncome}</CardContent>
                </Card>
                <Card>
                    <CardHeader><CardTitle>Total Expenses</CardTitle></CardHeader>
                    <CardContent className="text-2xl">${summary.totalExpenses}</CardContent>
                </Card>
                <Card>
                    <CardHeader><CardTitle>Savings Rate</CardTitle></CardHeader>
                    <CardContent className="text-2xl">{summary.savingsRate * 100}%</CardContent>
                </Card>
            </div>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                <CashFlowChart />
                <RecommendationList />
            </div>
        </div>
    );
}
