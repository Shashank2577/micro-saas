"use client";

import React, { useEffect, useState } from 'react';
import PageHeader from '../../components/PageHeader';
import DataTable from '../../components/DataTable';

export default function ApprovalRecordsPage() {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/v1/compensation-benchmark/approval-records')
            .then(res => {
                if (res.ok) return res.json();
                return [];
            })
            .then(data => {
                setData(data);
                setLoading(false);
            })
            .catch(() => setLoading(false));
    }, []);

    return (
        <div className="p-8">
            <PageHeader title="Approval Records" description="Track compensation adjustment approvals." />
            {loading ? <p>Loading...</p> : <DataTable data={data} columns={['id', 'name', 'status', 'createdAt']} />}
        </div>
    );
}
