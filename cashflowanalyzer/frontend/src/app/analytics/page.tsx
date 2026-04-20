"use client";

import React from 'react';
import ExpenseBreakdownPie from "@/components/ExpenseBreakdownPie";

export default function AnalyticsPage() {
    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Analytics</h1>
            <ExpenseBreakdownPie />
        </div>
    );
}
