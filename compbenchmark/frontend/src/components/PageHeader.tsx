import React from 'react';

export default function PageHeader({ title, description }: { title: string, description?: string }) {
    return (
        <div className="mb-6">
            <h1 className="text-2xl font-bold">{title}</h1>
            {description && <p className="text-gray-600">{description}</p>}
        </div>
    );
}
