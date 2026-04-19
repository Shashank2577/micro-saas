"use client";

import React, { useEffect, useState } from 'react';
import PageHeader from '../../components/PageHeader';
import DataTable from '../../components/DataTable';

export default function BenchmarkRunsPage() {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/v1/compensation-benchmark/benchmark-runs')
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
            <PageHeader title="Benchmark Runs" description="History of automated benchmark runs." />
            {loading ? <p>Loading...</p> : <DataTable data={data} columns={['id', 'name', 'status', 'createdAt']} />}
        </div>
    );
}
