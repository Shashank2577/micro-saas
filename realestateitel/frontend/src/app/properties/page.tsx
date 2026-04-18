"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';
import Link from 'next/link';

export default function PropertiesPage() {
    const [properties, setProperties] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get('/properties')
            .then(res => {
                setProperties(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, []);

    if (loading) return <div>Loading properties...</div>;

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Properties</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {properties.map((prop: any) => (
                    <div key={prop.id} className="border rounded-lg p-6 shadow-sm">
                        <h2 className="text-xl font-semibold mb-2">{prop.address}</h2>
                        <p className="text-gray-600 mb-4">{prop.city}, {prop.state} {prop.zipCode}</p>
                        <div className="flex justify-between items-center text-sm">
                            <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded">{prop.status}</span>
                            <span className="text-gray-500">{prop.propertyType}</span>
                        </div>
                        <div className="mt-4 pt-4 border-t flex justify-between">
                            <Link href={`/properties/${prop.id}`} className="text-blue-600 hover:underline">
                                View Details
                            </Link>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
