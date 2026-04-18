"use client";

import { useState, useEffect } from 'react';
import Link from 'next/link';
import api from '@/lib/api';

export default function Dashboard() {
    const [marketTrends, setMarketTrends] = useState<any[]>([]);
    
    useEffect(() => {
        // Fetching trends for a default zip code
        api.get('/market-trends/90210')
            .then(res => setMarketTrends(res.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="p-8 max-w-6xl mx-auto">
            <h1 className="text-4xl font-bold mb-8">RealEstateIntel Dashboard</h1>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
                <Link href="/properties" className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow cursor-pointer block">
                    <h2 className="text-lg font-semibold text-gray-700 mb-2">Properties</h2>
                    <p className="text-3xl font-bold text-blue-600">View All</p>
                    <p className="text-sm text-gray-500 mt-2">Manage portfolio & view AI comps</p>
                </Link>
                
                <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <h2 className="text-lg font-semibold text-gray-700 mb-2">Portfolio Health</h2>
                    <p className="text-3xl font-bold text-green-600">92%</p>
                    <p className="text-sm text-gray-500 mt-2">Based on current vs market rent</p>
                </div>
                
                <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <h2 className="text-lg font-semibold text-gray-700 mb-2">Active Leases</h2>
                    <p className="text-3xl font-bold text-purple-600">14</p>
                    <p className="text-sm text-gray-500 mt-2">2 expiring in next 90 days</p>
                </div>
            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-8">
                <h2 className="text-2xl font-bold mb-6">Market Trends (Zip: 90210)</h2>
                
                {marketTrends.length === 0 ? (
                    <p className="text-gray-500 italic">No trend data available. Add data to visualize trends.</p>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead>
                                <tr className="border-b">
                                    <th className="pb-3 text-gray-600 font-medium">Month</th>
                                    <th className="pb-3 text-gray-600 font-medium">Median Sale Price</th>
                                    <th className="pb-3 text-gray-600 font-medium">Days on Market</th>
                                    <th className="pb-3 text-gray-600 font-medium">Inventory</th>
                                </tr>
                            </thead>
                            <tbody>
                                {marketTrends.map(trend => (
                                    <tr key={trend.id} className="border-b last:border-0">
                                        <td className="py-4 font-medium">{new Date(trend.monthYear).toLocaleDateString(undefined, {month: 'short', year: 'numeric'})}</td>
                                        <td className="py-4">${trend.medianSalePrice.toLocaleString()}</td>
                                        <td className="py-4">{trend.daysOnMarket} days</td>
                                        <td className="py-4">{trend.inventoryCount}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}
