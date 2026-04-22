'use client';
import { useState } from 'react';

export default function GeneratePage() {
    const [prompt, setPrompt] = useState('');
    const [result, setResult] = useState('');
    const [loading, setLoading] = useState(false);

    const handleGenerate = async () => {
        setLoading(true);
        try {
            const res = await fetch('/api/generate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Tenant-ID': 'default-tenant'
                },
                body: JSON.stringify({ prompt, status: 'PENDING' })
            });
            const data = await res.json();
            setResult(data.result || 'Failed to generate');
        } catch (err) {
            console.error(err);
            setResult('Error occurred during generation.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-8 max-w-2xl mx-auto">
            <h1 className="text-2xl font-bold mb-4">Generate Content</h1>
            <textarea
                className="w-full border p-2 rounded mb-4 h-32 text-black"
                placeholder="What would you like to write?"
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
            />
            <button
                className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
                onClick={handleGenerate}
                disabled={loading || !prompt.trim()}
            >
                {loading ? 'Generating...' : 'Generate'}
            </button>

            {result && (
                <div className="mt-8">
                    <h2 className="text-xl font-semibold mb-2">Result:</h2>
                    <div className="p-4 bg-gray-100 rounded border whitespace-pre-wrap text-black">
                        {result}
                    </div>
                </div>
            )}
        </div>
    );
}
