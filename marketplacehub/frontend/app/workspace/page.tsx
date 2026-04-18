"use client";

import { useEffect, useState } from "react";
import { BASE_URL } from "../../lib/api";

export default function WorkspaceDashboard() {
    const [installations, setInstallations] = useState<any[]>([]);

    useEffect(() => {
        loadInstallations();
    }, []);

    const loadInstallations = async () => {
        try {
            const res = await fetch(`${BASE_URL}/installations/workspace/ws-123`);
            if (res.ok) {
                const data = await res.json();
                setInstallations(data);
            }
        } catch (e) {
            console.error(e);
        }
    };

    return (
        <main className="p-8">
            <h1 className="text-3xl font-bold mb-4">My Workspace</h1>
            <h2 className="text-2xl font-semibold mb-4">Installed Apps</h2>
            {installations.length === 0 ? (
                <p>No apps installed yet.</p>
            ) : (
                <ul className="list-disc pl-5">
                    {installations.map(inst => (
                        <li key={inst.id} className="mb-2">
                            App ID: {inst.appId} - Status: {inst.status} - Trial Ends: {new Date(inst.trialEndsAt).toLocaleDateString()}
                        </li>
                    ))}
                </ul>
            )}
        </main>
    );
}
