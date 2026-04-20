"use client";

import React from 'react';
import BudgetComparison from "@/components/BudgetComparison";

export default function BudgetPage() {
    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Budgets</h1>
            <BudgetComparison />
        </div>
    );
}
