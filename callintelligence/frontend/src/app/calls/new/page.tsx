'use client';
import { useState, useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { api } from '@/lib/api';
import { useRouter } from 'next/navigation';

export default function UploadCallPage() {
    const router = useRouter();
    const [title, setTitle] = useState('');
    const [repId, setRepId] = useState('rep-1');
    const [uploading, setUploading] = useState(false);
    
    const onDrop = useCallback((acceptedFiles: File[]) => {
        // In a real app we'd upload the file to S3/MinIO here
        console.log(acceptedFiles);
    }, []);

    const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setUploading(true);
        try {
            const call = await fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8143'}/api/calls`, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
                },
                body: JSON.stringify({
                    title,
                    repId,
                    audioUrl: 'http://example.com/audio.mp3', // Mock URL
                    durationSeconds: 300
                })
            }).then(res => res.json());
            
            router.push(`/calls/${call.id}`);
        } catch (err) {
            console.error(err);
            setUploading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto space-y-6">
            <h1 className="text-2xl font-bold">Upload Call Recording</h1>
            <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded-lg shadow">
                <div>
                    <label className="block text-sm font-medium text-gray-700">Call Title</label>
                    <input 
                        type="text" 
                        required
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" 
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Rep ID</label>
                    <input 
                        type="text" 
                        required
                        value={repId}
                        onChange={e => setRepId(e.target.value)}
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" 
                    />
                </div>
                
                <div {...getRootProps()} className={`border-2 border-dashed rounded-lg p-10 text-center cursor-pointer ${isDragActive ? 'border-indigo-500 bg-indigo-50' : 'border-gray-300 hover:bg-gray-50'}`}>
                    <input {...getInputProps()} />
                    {isDragActive ? (
                        <p className="text-indigo-600">Drop the audio file here ...</p>
                    ) : (
                        <p className="text-gray-500">Drag 'n' drop an audio file here, or click to select files</p>
                    )}
                </div>

                <div className="pt-4">
                    <button 
                        type="submit" 
                        disabled={uploading}
                        className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
                    >
                        {uploading ? 'Processing...' : 'Upload & Analyze'}
                    </button>
                </div>
            </form>
        </div>
    );
}
