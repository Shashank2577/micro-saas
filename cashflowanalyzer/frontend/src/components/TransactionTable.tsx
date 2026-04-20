import React, { useState, useEffect } from 'react';
import { Card, CardContent } from "@/components/ui/card";

export default function TransactionTable() {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        fetch('/api/transactions', { headers: { 'X-Tenant-ID': 'tenant-1' } })
            .then(res => res.json())
            .then(data => setTransactions(data))
            .catch(console.error);
    }, []);

    return (
        <Card>
            <CardContent>
                <table className="w-full text-left">
                    <thead>
                        <tr>
                            <th className="py-2">Date</th>
                            <th className="py-2">Merchant</th>
                            <th className="py-2">Category</th>
                            <th className="py-2">Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        {transactions.map((t: any) => (
                            <tr key={t.id} className="border-t">
                                <td className="py-2">{t.date}</td>
                                <td className="py-2">{t.merchantName || t.name}</td>
                                <td className="py-2">{t.category?.name || 'Uncategorized'}</td>
                                <td className="py-2">${t.amount}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </CardContent>
        </Card>
    );
}
