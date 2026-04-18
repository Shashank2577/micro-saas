import { Framework } from '@/lib/api';
import { useState } from 'react';

export default function AuditPackageGenerator({ frameworks, onGenerate }: { frameworks: Framework[], onGenerate: (frameworkId: string, name: string) => void }) {
    const [selectedFramework, setSelectedFramework] = useState(frameworks[0]?.id || '');
    const [name, setName] = useState('');

    return (
        <div className="mb-8 p-4 border rounded bg-gray-50">
            <h2 className="text-xl font-semibold mb-4">Generate Audit Package</h2>
            <select value={selectedFramework} onChange={(e) => setSelectedFramework(e.target.value)} className="border p-2 mr-4">
                {frameworks.map(f => <option key={f.id} value={f.id}>{f.name}</option>)}
            </select>
            <input type="text" placeholder="Package Name" value={name} onChange={(e) => setName(e.target.value)} className="border p-2 mr-4" />
            <button onClick={() => onGenerate(selectedFramework, name)} className="bg-purple-500 text-white px-4 py-2 rounded">Generate</button>
        </div>
    );
}
