import React, { useState } from 'react';

interface DataTableProps {
    data: any[];
    columns: string[];
    onEdit?: (id: string) => void;
}

export function DataTable({ data, columns, onEdit }: DataTableProps) {
    const [page, setPage] = useState(1);
    const pageSize = 5;
    const paginatedData = data.slice((page - 1) * pageSize, page * pageSize);

    return (
        <div className="w-full">
            <table className="min-w-full bg-white border">
                <thead>
                    <tr>
                        {columns.map(c => <th key={c} className="border p-2">{c}</th>)}
                        <th className="border p-2">Actions</th>
                    </tr>
                </thead>
                <tbody data-testid="data-list">
                    {paginatedData.map((row, i) => (
                        <tr key={row.id || i}>
                            {columns.map(c => <td key={c} className="border p-2">{row[c.toLowerCase()]}</td>)}
                            <td className="border p-2">
                                {onEdit && <button onClick={() => onEdit(row.id)} className="text-blue-500 underline">Edit</button>}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <div className="flex justify-between items-center mt-2">
                <button
                    disabled={page === 1}
                    onClick={() => setPage(p => p - 1)}
                    className="p-2 bg-gray-200 rounded disabled:opacity-50">
                    Previous
                </button>
                <span>Page {page}</span>
                <button
                    disabled={page * pageSize >= data.length}
                    onClick={() => setPage(p => p + 1)}
                    className="p-2 bg-gray-200 rounded disabled:opacity-50">
                    Next
                </button>
            </div>
        </div>
    );
}
