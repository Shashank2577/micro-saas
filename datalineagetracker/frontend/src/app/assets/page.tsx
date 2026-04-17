"use client";

import { useEffect, useState } from 'react';
import { DataAsset } from '@/types';
import Link from 'next/link';

export default function AssetsPage() {
    const [assets, setAssets] = useState<DataAsset[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('http://localhost:8167/api/v1/assets', {
            headers: {
                'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
            }
        })
        .then(res => res.json())
        .then(data => {
            setAssets(data);
            setLoading(false);
        })
        .catch(err => {
            console.error(err);
            setLoading(false);
        });
    }, []);

    if (loading) return <div className="p-8">Loading assets...</div>;

    return (
        <div className="max-w-7xl mx-auto p-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Data Assets</h1>
                <button className="bg-indigo-600 text-white px-4 py-2 rounded">Add Asset</button>
            </div>
            
            <div className="bg-white shadow overflow-hidden sm:rounded-md">
                <ul className="divide-y divide-gray-200">
                    {assets.map(asset => (
                        <li key={asset.id}>
                            <div className="px-4 py-4 sm:px-6 hover:bg-gray-50 flex items-center justify-between">
                                <div>
                                    <h3 className="text-lg font-medium text-indigo-600 truncate">{asset.name}</h3>
                                    <div className="mt-2 flex items-center text-sm text-gray-500">
                                        <span className="mr-4">Type: <span className="font-semibold">{asset.type}</span></span>
                                        <span className="mr-4">Class: <span className="font-semibold">{asset.classification}</span></span>
                                        <span>Source: {asset.sourceSystem}</span>
                                    </div>
                                </div>
                                <div className="flex space-x-4">
                                    <Link href={`/lineage?assetId=${asset.id}`} className="text-indigo-600 hover:text-indigo-900">
                                        View Lineage
                                    </Link>
                                    <button className="text-green-600 hover:text-green-900">Scan PII</button>
                                </div>
                            </div>
                        </li>
                    ))}
                    {assets.length === 0 && (
                        <li className="px-4 py-8 text-center text-gray-500">No assets found.</li>
                    )}
                </ul>
            </div>
        </div>
    );
}
