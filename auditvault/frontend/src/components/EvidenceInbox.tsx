import { Evidence } from '@/lib/api';

export default function EvidenceInbox({ evidence, onMap }: { evidence: Evidence[], onMap: (e: Evidence) => void }) {
    return (
        <div className="mb-8">
            <h2 className="text-xl font-semibold mb-4">Evidence Inbox</h2>
            <div className="grid grid-cols-1 gap-4">
                {evidence.map(e => (
                    <div key={e.id} className="border p-4 rounded shadow">
                        <h3 className="font-bold">{e.sourceApp}</h3>
                        <p>{e.content}</p>
                        <p>Status: {e.status}</p>
                        {e.status === 'PENDING_MAPPING' && (
                            <button onClick={() => onMap(e)} className="mt-2 bg-green-500 text-white px-4 py-2 rounded">Map Evidence</button>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}
