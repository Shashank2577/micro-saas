"use client";

import React from 'react';
import TransactionTable from "@/components/TransactionTable";

export default function TransactionsPage() {
    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Transactions</h1>
            <TransactionTable />
        </div>
    );
}
