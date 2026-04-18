"use client";

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import api from '@/lib/api';
import Link from 'next/link';

export default function PropertyDetailsPage() {
    const params = useParams();
    const [property, setProperty] = useState<any>(null);
    const [comparables, setComparables] = useState<any[]>([]);
    const [leases, setLeases] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [generating, setGenerating] = useState(false);

    useEffect(() => {
        if (!params.id) return;
        
        const fetchData = async () => {
            try {
                const [propRes, compRes, leaseRes] = await Promise.all([
                    api.get(`/properties/${params.id}`),
                    api.get(`/properties/${params.id}/comparables`),
                    api.get(`/properties/${params.id}/leases`)
                ]);
                setProperty(propRes.data);
                setComparables(compRes.data);
                setLeases(leaseRes.data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [params.id]);

    const handleGenerateComps = async () => {
        setGenerating(true);
        try {
            const res = await api.post(`/properties/${params.id}/comparables/generate`);
            setComparables(res.data);
        } catch (err) {
            console.error(err);
        } finally {
            setGenerating(false);
        }
    };

    if (loading) return <div>Loading...</div>;
    if (!property) return <div>Property not found</div>;

    return (
        <div className="p-8 max-w-6xl mx-auto">
            <div className="mb-8">
                <Link href="/properties" className="text-blue-600 hover:underline">&larr; Back to Properties</Link>
            </div>
            
            <div className="bg-white rounded-xl shadow-md overflow-hidden mb-8 border border-gray-100">
                <div className="p-8">
                    <div className="flex justify-between items-start">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{property.address}</h1>
                            <p className="text-xl text-gray-600">{property.city}, {property.state} {property.zipCode}</p>
                        </div>
                        <span className="bg-green-100 text-green-800 px-4 py-2 rounded-full font-semibold">{property.status}</span>
                    </div>
                    
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-6 mt-8">
                        <div className="bg-gray-50 p-4 rounded-lg">
                            <p className="text-sm text-gray-500 font-medium">Type</p>
                            <p className="text-lg font-semibold">{property.propertyType}</p>
                        </div>
                        <div className="bg-gray-50 p-4 rounded-lg">
                            <p className="text-sm text-gray-500 font-medium">Beds / Baths</p>
                            <p className="text-lg font-semibold">{property.bedrooms} / {property.bathrooms}</p>
                        </div>
                        <div className="bg-gray-50 p-4 rounded-lg">
                            <p className="text-sm text-gray-500 font-medium">Sq Ft</p>
                            <p className="text-lg font-semibold">{property.squareFeet}</p>
                        </div>
                        <div className="bg-gray-50 p-4 rounded-lg">
                            <p className="text-sm text-gray-500 font-medium">Year Built</p>
                            <p className="text-lg font-semibold">{property.yearBuilt}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                {/* Comparables Section */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                    <div className="flex justify-between items-center mb-6">
                        <h2 className="text-2xl font-bold">Comparables</h2>
                        <button 
                            onClick={handleGenerateComps}
                            disabled={generating}
                            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium transition-colors disabled:opacity-50"
                        >
                            {generating ? 'Generating...' : 'AI Generate Comps'}
                        </button>
                    </div>
                    
                    {comparables.length === 0 ? (
                        <p className="text-gray-500 italic text-center py-8">No comparables generated yet.</p>
                    ) : (
                        <div className="space-y-4">
                            {comparables.map((comp) => (
                                <div key={comp.id} className="border border-gray-200 rounded-lg p-4">
                                    <div className="flex justify-between mb-2">
                                        <span className="font-semibold">{comp.compProperty.address}</span>
                                        <span className="text-blue-600 font-bold">${comp.priceAdjusted?.toLocaleString()}</span>
                                    </div>
                                    <div className="flex items-center gap-2 mb-3">
                                        <div className="bg-gray-200 h-2 w-full rounded-full overflow-hidden">
                                            <div 
                                                className="bg-blue-600 h-full" 
                                                style={{ width: `${(comp.similarityScore * 100)}%` }}
                                            />
                                        </div>
                                        <span className="text-sm text-gray-600 font-medium whitespace-nowrap">
                                            {(comp.similarityScore * 100).toFixed(0)}% Match
                                        </span>
                                    </div>
                                    <p className="text-sm text-gray-600 bg-gray-50 p-3 rounded">{comp.notes}</p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {/* Leases Section */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                    <div className="flex justify-between items-center mb-6">
                        <h2 className="text-2xl font-bold">Active Leases</h2>
                        <Link 
                            href={`/leases/upload?propertyId=${property.id}`}
                            className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
                        >
                            + Upload Lease
                        </Link>
                    </div>
                    
                    {leases.length === 0 ? (
                        <p className="text-gray-500 italic text-center py-8">No active leases.</p>
                    ) : (
                        <div className="space-y-4">
                            {leases.map((lease) => (
                                <div key={lease.id} className="border border-gray-200 rounded-lg p-5">
                                    <div className="flex justify-between items-center mb-4">
                                        <h3 className="font-bold text-lg">{lease.tenantName}</h3>
                                        <span className="text-green-600 font-bold text-lg">${lease.monthlyRent?.toLocaleString()}/mo</span>
                                    </div>
                                    <div className="grid grid-cols-2 gap-4 mb-4 text-sm">
                                        <div>
                                            <p className="text-gray-500">Start Date</p>
                                            <p className="font-medium">{new Date(lease.startDate).toLocaleDateString()}</p>
                                        </div>
                                        <div>
                                            <p className="text-gray-500">End Date</p>
                                            <p className="font-medium">{new Date(lease.endDate).toLocaleDateString()}</p>
                                        </div>
                                    </div>
                                    <div className="bg-blue-50 p-4 rounded-lg border border-blue-100">
                                        <p className="text-xs font-semibold text-blue-800 uppercase mb-2">AI Abstract</p>
                                        <p className="text-sm text-blue-900">{lease.aiSummary}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
