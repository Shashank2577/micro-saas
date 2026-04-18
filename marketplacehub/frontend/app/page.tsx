"use client";

import { useEffect, useState } from "react";
import { fetchApps, fetchTrendingApps } from "../lib/api";

export default function MarketplaceHubHome() {
    const [apps, setApps] = useState<any[]>([]);
    const [trendingApps, setTrendingApps] = useState<any[]>([]);
    const [searchText, setSearchText] = useState("");

    useEffect(() => {
        loadApps();
        loadTrending();
    }, []);

    const loadApps = async (search = "") => {
        try {
            const data = await fetchApps(undefined, search);
            setApps(data);
        } catch (e) {
            console.error(e);
        }
    };

    const loadTrending = async () => {
        try {
            const data = await fetchTrendingApps();
            setTrendingApps(data);
        } catch (e) {
            console.error(e);
        }
    };

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        loadApps(searchText);
    };

    return (
        <main className="p-8">
            <h1 className="text-3xl font-bold mb-4">MarketplaceHub</h1>
            
            <form onSubmit={handleSearch} className="mb-8">
                <input 
                    type="text" 
                    value={searchText} 
                    onChange={e => setSearchText(e.target.value)} 
                    placeholder="Search apps..." 
                    className="border p-2 rounded mr-2"
                />
                <button type="submit" className="bg-blue-500 text-white p-2 rounded">Search</button>
            </form>

            <h2 className="text-2xl font-semibold mb-4">Trending Apps</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
                {trendingApps.map(app => (
                    <div key={app.id} className="border p-4 rounded shadow">
                        <h3 className="text-xl font-bold">{app.name}</h3>
                        <p>{app.description}</p>
                        <a href={`/apps/${app.id}`} className="text-blue-500 mt-2 block">View Details</a>
                    </div>
                ))}
            </div>

            <h2 className="text-2xl font-semibold mb-4">All Apps</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {apps.map(app => (
                    <div key={app.id} className="border p-4 rounded shadow">
                        <h3 className="text-xl font-bold">{app.name}</h3>
                        <p>{app.category}</p>
                        <a href={`/apps/${app.id}`} className="text-blue-500 mt-2 block">View Details</a>
                    </div>
                ))}
            </div>
        </main>
    );
}
