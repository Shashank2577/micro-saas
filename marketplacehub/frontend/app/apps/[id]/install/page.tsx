"use client";

import { useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { installApp } from "../../../../lib/api";

export default function InstallAppPage() {
    const params = useParams();
    const router = useRouter();
    const id = params.id as string;
    
    const [workspaceId, setWorkspaceId] = useState("");
    const permissions = ["read:user_profile", "write:app_data"];

    const handleInstall = async () => {
        try {
            await installApp(id, workspaceId, permissions);
            alert("App installed successfully! A 14-day trial has started.");
            router.push(`/apps/${id}`);
        } catch (e) {
            console.error(e);
            alert("Failed to install app");
        }
    };

    return (
        <main className="p-8">
            <h1 className="text-3xl font-bold mb-4">Install Application</h1>
            <p className="mb-4">You are about to install this application. It requests the following permissions:</p>
            <ul className="list-disc pl-5 mb-4">
                {permissions.map(p => <li key={p}>{p}</li>)}
            </ul>
            
            <div className="mb-4">
                <label className="block mb-2">Workspace ID</label>
                <input 
                    type="text" 
                    value={workspaceId} 
                    onChange={e => setWorkspaceId(e.target.value)} 
                    placeholder="Enter Workspace ID" 
                    className="border p-2 rounded"
                />
            </div>
            
            <button onClick={handleInstall} className="bg-green-500 text-white px-4 py-2 rounded mr-2">Approve and Install</button>
            <button onClick={() => router.push(`/apps/${id}`)} className="bg-gray-500 text-white px-4 py-2 rounded">Cancel</button>
        </main>
    );
}
