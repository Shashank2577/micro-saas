'use client';

import { useEffect, useState } from 'react';
import { api, Framework, Evidence, AuditPackage } from '@/lib/api';
import FrameworkListPage from '@/components/FrameworkListPage';
import EvidenceInbox from '@/components/EvidenceInbox';
import AuditPackageGenerator from '@/components/AuditPackageGenerator';

export default function DashboardPage() {
    const [frameworks, setFrameworks] = useState<Framework[]>([]);
    const [evidence, setEvidence] = useState<Evidence[]>([]);
    const [packages, setPackages] = useState<AuditPackage[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadData = async () => {
            try {
                const [f, e, p] = await Promise.all([
                    api.frameworks.list(),
                    api.evidence.list(),
                    api.packages.list()
                ]);
                setFrameworks(f);
                setEvidence(e);
                setPackages(p);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, []);

    const handleMapEvidence = async (evidenceId: string, frameworkId: string) => {
        try {
            await api.evidence.map(evidenceId, frameworkId);
            const e = await api.evidence.list();
            setEvidence(e);
        } catch (err) {
            console.error(err);
        }
    };

    const handleGeneratePackage = async (frameworkId: string, name: string) => {
        try {
            await api.packages.generate(frameworkId, name);
            const p = await api.packages.list();
            setPackages(p);
        } catch (err) {
            console.error(err);
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div className="p-8 max-w-6xl mx-auto">
            <h1 className="text-3xl font-bold mb-8">AuditVault Dashboard</h1>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div>
                    <FrameworkListPage frameworks={frameworks} onSelect={() => {}} />
                    <AuditPackageGenerator frameworks={frameworks} onGenerate={handleGeneratePackage} />
                </div>
                <div>
                    <EvidenceInbox evidence={evidence} onMap={(e) => frameworks.length > 0 && handleMapEvidence(e.id, frameworks[0].id)} />
                    
                    <div className="mt-8">
                        <h2 className="text-xl font-semibold mb-4">Audit Packages</h2>
                        <ul>
                            {packages.map(p => (
                                <li key={p.id} className="border p-4 my-2 rounded shadow">
                                    {p.name} - {p.status} - <a href={p.downloadUrl} className="text-blue-500 underline ml-2">Download</a>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
}
