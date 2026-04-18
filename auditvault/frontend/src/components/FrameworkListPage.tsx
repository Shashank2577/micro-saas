import { Framework } from '@/lib/api';

export default function FrameworkListPage({ frameworks, onSelect }: { frameworks: Framework[], onSelect: (f: Framework) => void }) {
    return (
        <div className="mb-8">
            <h2 className="text-xl font-semibold mb-4">Frameworks</h2>
            <div className="grid grid-cols-1 gap-4">
                {frameworks.map(f => (
                    <div key={f.id} className="border p-4 rounded shadow">
                        <h3 className="font-bold">{f.name}</h3>
                        <p>{f.description}</p>
                        <p>Status: {f.status}</p>
                        <button onClick={() => onSelect(f)} className="mt-2 bg-blue-500 text-white px-4 py-2 rounded">View</button>
                    </div>
                ))}
            </div>
        </div>
    );
}
