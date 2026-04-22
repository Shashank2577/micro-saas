"use client";
import { useEffect, useState } from "react";
import { api } from "../lib/api";

export function PolicyList() {
    const [policies, setPolicies] = useState<any[]>([]);

    useEffect(() => {
        api.policies.list().then(data => setPolicies(data || []));
    }, []);

    return (
        <div>
            {policies.length === 0 ? (
                <p>No policies found.</p>
            ) : (
                <ul>
                    {policies.map(p => <li key={p.id}>{p.title}</li>)}
                </ul>
            )}
        </div>
    );
}
