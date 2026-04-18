"use client";

import { useState, Suspense } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import api from '@/lib/api';

function LeaseUploadForm() {
    const searchParams = useSearchParams();
    const router = useRouter();
    const propertyId = searchParams.get('propertyId');
    
    const [leaseText, setLeaseText] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!propertyId || !leaseText) {
            setError('Property ID and Lease Text are required');
            return;
        }

        setLoading(true);
        setError('');

        try {
            await api.post('/leases/upload', {
                propertyId,
                text: leaseText
            });
            router.push(`/properties/${propertyId}`);
        } catch (err) {
            console.error(err);
            setError('Failed to process lease. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto p-8 bg-white rounded-xl shadow-md mt-10">
            <h1 className="text-3xl font-bold mb-6">AI Lease Abstraction</h1>
            <p className="text-gray-600 mb-8">
                Paste the raw text of the lease agreement below. Our AI will analyze the document, 
                extract key terms (tenant, rent, dates), and generate a concise abstract.
            </p>

            {error && <div className="bg-red-50 text-red-600 p-4 rounded mb-6">{error}</div>}

            <form onSubmit={handleSubmit}>
                <div className="mb-6">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Lease Document Text
                    </label>
                    <textarea
                        value={leaseText}
                        onChange={(e) => setLeaseText(e.target.value)}
                        className="w-full h-64 p-4 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Paste lease text here..."
                        required
                    />
                </div>

                <div className="flex gap-4">
                    <button
                        type="button"
                        onClick={() => router.back()}
                        className="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        disabled={loading}
                        className="flex-1 bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-medium transition-colors disabled:opacity-50"
                    >
                        {loading ? 'Analyzing with AI...' : 'Process Lease'}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default function LeaseUploadPage() {
    return (
        <Suspense fallback={<div>Loading...</div>}>
            <LeaseUploadForm />
        </Suspense>
    );
}
